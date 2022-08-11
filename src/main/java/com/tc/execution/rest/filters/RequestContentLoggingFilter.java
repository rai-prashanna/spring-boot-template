package com.tc.execution.rest.filters;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class RequestContentLoggingFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(RequestContentLoggingFilter.class);

	private Logger requestLogger = LOG;

	String path;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		path = filterConfig.getServletContext().getContextPath();
		LOG.info("Init request content filter on path {}",path);
		String category = filterConfig.getInitParameter("log.category");
		if (category != null) {
			LOG.info("Reconfiguring response content logging category to {}",category);
			requestLogger = LoggerFactory.getLogger(category);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest newRequest = logContent((HttpServletRequest) request,(HttpServletResponse)response);
		chain.doFilter(newRequest, response);
	}

	private String getQueryString(HttpServletRequest request) {
		return request.getQueryString() != null ? "?" + request.getQueryString() : "";
	}

	private String getContentType(HttpServletRequest request) {
		return request.getContentType() != null ? "Content-Type: " + request.getContentType() : "";
	}

	private String getHeaderText(HttpServletRequest request, String header) {
		if (request.getHeader(header) == null) {
			return "";
		}
		return header + ": " + request.getHeader(header);
	}

	private HttpServletRequest logContent(HttpServletRequest request, HttpServletResponse response) {
		HttpServletRequest newRequest = null;
		try {
			if (requestLogger.isInfoEnabled()) {
				String requestURI = request.getRequestURI();
				if (requestURI.contains("/echo/") || requestURI.contains("health_check")) {
					LOG.trace("another echo from request {}", requestURI);
					return request;
				}
				if ("GET".equals(request.getMethod())) {
					requestLogger.info("Request GET {}{} {}", requestURI, getQueryString(request),getHeaderText(request, "Accept"));
					return request;
				} else if (request.getContentType() != null && request.getContentType().contains("form")) {
					requestLogger.info("Request {} {}{} {}",request.getMethod(), requestURI, getQueryString(request) ,getContentType(request));
					return request;
				} else {
					String decodedContent = null;
					String encoding = null;
					if (request.getContentLength() == 0) {
						decodedContent = "- no content -";
					} else if (request.getContentType() != null && (request.getContentType().contains("multipart") || request.getContentType().contains("octet-stream") || request.getContentType().contains("image"))) {
						decodedContent = " - binary content -";
					} else {
						InputStreamReader contentReader = new InputStreamReader(request.getInputStream());
						encoding = request.getCharacterEncoding();
						if (encoding != null && contentReader.ready()) {
							LOG.debug("Request contained {} character encoding",encoding);
							byte[] content = IOUtils.toByteArray(contentReader, encoding);
							newRequest = new HttpServletContentWrapper(request, content);
							decodedContent = Charset.forName(encoding).newDecoder().decode(ByteBuffer.wrap(content)).toString();
						} else if(contentReader.ready()) {
							LOG.debug("Request contained no character encoding, defaulting to UTF_8");
							byte[] content = IOUtils.toByteArray(contentReader,StandardCharsets.UTF_8 );
							newRequest = new HttpServletContentWrapper(request, content);
							decodedContent = new String(content);
						}
					}
					requestLogger.info("Request {} {}{} {} {} {}",request.getMethod(), requestURI, getQueryString(request) ,getContentType(request),encoding,decodedContent);
				}
			}
		} catch (IOException e) {
			LOG.error("Failed to read content from request",e);
		}
		if (newRequest == null) {
			return request;
		}
		return newRequest;
	}

	@Override
	public void destroy() {
		LOG.info("Destroy request content filter on path {}",path);
	}

	public static class HttpServletContentWrapper extends HttpServletRequestWrapper {

		byte[] content = null;


		public HttpServletContentWrapper(HttpServletRequest request, byte[] content) {
			super(request);
			this.content = content;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			final ByteArrayInputStream bais = new ByteArrayInputStream(content);
            String characterEncoding = this.getCharacterEncoding();
            if (characterEncoding == null) {
                return new BufferedReader(new InputStreamReader(bais));
            } else {
                return new BufferedReader(new InputStreamReader(bais, characterEncoding));
            }
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(content);
            ServletInputStream sis = new ServletInputStream() {
				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setReadListener(ReadListener listener) {

				}

				@Override
                public int read() throws IOException {
                    return bais.read();
                }

            };
	        return sis;
		}


	}


}

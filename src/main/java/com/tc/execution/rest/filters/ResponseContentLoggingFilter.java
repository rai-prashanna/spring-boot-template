package com.tc.execution.rest.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class ResponseContentLoggingFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseContentLoggingFilter.class);

	private Logger responseLogger = LOG;
	
	private int CONTENT_MAX_SIZE = 0;

	String path;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		path = filterConfig.getServletContext().getContextPath();
		LOG.info("Init request content filter on path {}",path);
		String category = filterConfig.getInitParameter("log.category");
		if (category != null) {
			LOG.info("Reconfiguring response content logging category to {}",category);
			responseLogger = LoggerFactory.getLogger(category);
		}
	}
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		// Fix for not breaking swagger
		String requestURI = httpRequest.getRequestURI();
		if (requestURI.contains("/echo/") || requestURI.endsWith("ad-import-api/") || requestURI.endsWith("management-api/") || requestURI.endsWith("provisioning-api/")) {
			chain.doFilter(request, response);
			return;
		}
		
		LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper(httpResponse);
		chain.doFilter(request, responseWrapper);
		
		String content = null;
		
		if (isBinaryContent(response.getContentType())) {
			content = " - binary content -";
		} else if (isFileContent(response.getContentType())) {
			content = " - file content -";
		} else if (response.getContentType() == null) {
			content = " - unknown content type -";
		} else  {
			if (responseWrapper.getContent().length() == 0) {
				content = "- no content -";
			} else {
				content = CONTENT_MAX_SIZE > 0 ? responseWrapper.getContent().substring(0, Math.min(responseWrapper.getContent().length(), CONTENT_MAX_SIZE)) : responseWrapper.getContent();
			}
		}


		responseLogger.info("Response status code={}, headers={}, Content-Type={}, character encoding {} and content={}", responseWrapper.getStatus(), responseWrapper.getHeaders(), responseWrapper.getContentType(), responseWrapper.getCharacterEncoding(), content);
		httpResponse.getOutputStream().write(responseWrapper.getContentAsBytes());		
	}
	
	private boolean isBinaryContent(String contentType) {
		return contentType != null && (contentType.contains("multipart") || contentType.contains("octet-stream") || contentType.contains("image") || contentType.contains("video"));
	}
	
	private boolean isFileContent(String contentType) {
		return contentType != null && (contentType.contains("yaml") || contentType.contains("html") || contentType.contains("javascript") || contentType.contains("css"));
	}

	@Override
	public void destroy() {
		responseLogger.info("Destroy request content filter on path {}",path);
	}

	public class LoggingHttpServletResponseWrapper extends HttpServletResponseWrapper {

		private final LoggingServletOutpuStream loggingServletOutpuStream = new LoggingServletOutpuStream();

		private final HttpServletResponse delegate;

		public LoggingHttpServletResponseWrapper(HttpServletResponse response) {
			super(response);
			delegate = response;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return loggingServletOutpuStream;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return new PrintWriter(loggingServletOutpuStream.baos);
		}

		public Map<String, String> getHeaders() {
			Map<String, String> headers = new HashMap<>(0);
			for (String headerName : getHeaderNames()) {
				headers.put(headerName, getHeader(headerName));
			}
			return headers;
		}

		public String getContent() {
			try {
				String responseEncoding = delegate.getCharacterEncoding();
				return loggingServletOutpuStream.baos.toString(responseEncoding != null ? responseEncoding : UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				return "[UNSUPPORTED ENCODING]";
			}
		}

		public byte[] getContentAsBytes() {
			return loggingServletOutpuStream.baos.toByteArray();
		}

		private class LoggingServletOutpuStream extends ServletOutputStream {

			private ByteArrayOutputStream baos = new ByteArrayOutputStream();

			@Override
			public void write(int b) throws IOException {
				baos.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				baos.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				baos.write(b, off, len);
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener listener) {

			}
		}
	}
}

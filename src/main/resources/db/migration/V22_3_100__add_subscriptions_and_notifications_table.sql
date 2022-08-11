CREATE TABLE IF NOT EXISTS `subscriptions`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `subscription_id` VARCHAR(255),
    `operator_id` VARCHAR(255),
    `webhook_endpoint` VARCHAR(255),
    `aad_application_id` VARCHAR(255),
    `aad_tenant_id` VARCHAR(255),
    `created` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `notifications`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `notification_id` VARCHAR(255),
    `topic` VARCHAR(255),
    `subject` VARCHAR(255),
    `data` TEXT,
    `event_type` VARCHAR(255),
    `event_time` VARCHAR(255),
    `metadata_version` VARCHAR(255),
    `data_version` VARCHAR(255),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

package thespeace.practice.spring.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * <h1>JPA auditor enable</h1>
 */
@Configuration
@EnableJpaAuditing // JpaAuditing을 Enable
public class JpaAuditorConfig {
}

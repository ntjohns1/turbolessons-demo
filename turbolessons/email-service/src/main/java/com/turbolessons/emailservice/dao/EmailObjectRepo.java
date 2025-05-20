package com.turbolessons.emailservice.dao;

import com.turbolessons.emailservice.dto.MailObject;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmailObjectRepo extends ReactiveMongoRepository<MailObject, String> {
}

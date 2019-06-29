package com.capgemini.store.persistence.entity;

import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
//@AllArgsConstructor
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	private LocalDateTime createDate;

	private LocalDateTime updateDate;

	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
		this.updateDate = createDate;
	}

	@PreUpdate
	public void updateDate() {
		this.updateDate = LocalDateTime.now();
	}

}

package com.gatar;

import com.gatar.domain.Barcode;
import com.gatar.domain.Item;
import com.gatar.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpizarkaServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpizarkaServletApplication.class, args);
	}
}


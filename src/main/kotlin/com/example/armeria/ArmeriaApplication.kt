package com.example.armeria

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArmeriaApplication

fun main(args: Array<String>) {
	runApplication<ArmeriaApplication>(*args)
}

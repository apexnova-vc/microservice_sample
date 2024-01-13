package com.apexnova.sample.db

import org.springframework.stereotype.Service

@Service
class DbService {
    fun getData(id: String): String {
        // Implement your database logic here
        // For example, query your database using JPA, JDBC, etc.
        return "Data from DB for ID: $id"
    }
}

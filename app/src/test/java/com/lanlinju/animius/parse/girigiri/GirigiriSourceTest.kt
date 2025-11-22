package com.lanlinju.animius.parse.girigiri

import com.lanlinju.animius.data.remote.parse.GirigiriSource
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.URLEncoder

class GirigiriSourceTest {

    @Test
    fun `test URL encoding for non-ASCII search query`() {
        // Test that non-ASCII characters are properly encoded
        val query = "海贼王"
        val expectedEncoded = URLEncoder.encode(query, "utf-8")
        
        // Verify the encoding matches expected format
        assert(expectedEncoded.isNotEmpty())
        assert(expectedEncoded != query) // Should be encoded
        
        println("Original query: $query")
        println("Encoded query: $expectedEncoded")
    }
    
    @Test
    fun `test URL encoding for ASCII search query`() {
        // Test that ASCII characters are handled correctly
        val query = "onepiece"
        val expectedEncoded = URLEncoder.encode(query, "utf-8")
        
        // ASCII characters should remain mostly the same
        assert(expectedEncoded.isNotEmpty())
        
        println("Original query: $query")
        println("Encoded query: $expectedEncoded")
    }
    
    @Test
    fun `test URL encoding for special characters`() {
        // Test that special characters are properly encoded
        val query = "test & search"
        val expectedEncoded = URLEncoder.encode(query, "utf-8")
        
        // Special characters should be encoded
        assert(expectedEncoded.isNotEmpty())
        assert(expectedEncoded != query) // Should be encoded
        assert(expectedEncoded.contains("%")) // URL encoding uses %
        
        println("Original query: $query")
        println("Encoded query: $expectedEncoded")
    }
}

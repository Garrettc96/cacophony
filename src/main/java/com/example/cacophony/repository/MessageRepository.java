package com.example.cacophony.repository;

import com.example.cacophony.data.model.Message;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends ListCrudRepository<Message, UUID> {
    public List<Message> findByConversationIdAndCreatedAtBetween(UUID id, OffsetDateTime startTime,
            OffsetDateTime endTime);

    /**
     * Performs a full-text search using plainto_tsquery. 'plainto_tsquery' is often safer as it parses the input
     * string, removes punctuation, converts to lowercase, and handles operators simply. Use 'to_tsquery' if you need
     * more complex query syntax (AND, OR, NOT, phrase search). Use 'websearch_to_tsquery' for syntax similar to web
     * search engines.
     *
     * @param query
     *            The search terms.
     * @param language
     *            The text search configuration language (e.g., 'english').
     *
     * @return A list of matching Posts.
     */
    @Query(value = "SELECT * FROM message m WHERE conversation_id = :conversationId AND m.fts_vector @@ plainto_tsquery('english', :query)", nativeQuery = true)
    List<Message> searchByFullText(@Param("conversationId") UUID conversationId, @Param("query") String query

    );

    // Example using to_tsquery (allows operators like '&' for AND, '|' for OR)
    @Query(value = "SELECT * FROM message m WHERE conversation_id = :conversationId AND m.fts_vector @@ to_tsquery('english', :query)", nativeQuery = true)
    List<Message> searchWithOperators(@Param("conversationId") UUID conversationId, @Param("query") String query

    );

    // Example ranking results (higher rank means better match)
    @Query(value = """
            SELECT m.*, ts_rank_cd(m.fts_vector, plainto_tsquery(:query)) as rank
            FROM message m
            WHERE conversation_id = :conversationId AND m.fts_vector @@ plainto_tsquery(:query)
            ORDER BY rank DESC
            """, nativeQuery = true)
    List<Message> searchAndRankByFullText(@Param("conversationId") UUID conversationId, @Param("query") String query

    );

}

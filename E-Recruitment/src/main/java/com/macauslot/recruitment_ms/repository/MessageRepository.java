package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message,Integer> {

    @Query(value =
            "select message.chn_desc,message.id from message,message_group where message_group.description= :description and message.message_group_id=message_group.id and message.status='A'"
//            "select message.chn_desc, message.id from message, message_group, applicant_source_type ast where message_group.description = :description and message.message_group_id = message_group.id and message.status = 'A' and ast.message_id = message.ID order by ast.order_num"

            , nativeQuery = true)
    List<Object[]> findMsgDesc( @Param("description") String description);







}

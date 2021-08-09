package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message,Integer>,MessageRepositoryCustom {

    @Query(value = "select message.chn_desc,message.id from message,message_group where message_group.description= :description and message.message_group_id=message_group.id and message.status='A'", nativeQuery = true)
    List<Object[]> findMsgDesc( @Param("description") String description);


    @Query(value = "select message.status, message_group.DESCRIPTION, message.chn_desc,message.id from message,message_group where message_group.description in ( :descriptionList) and message.message_group_id=message_group.id order by message_group.id, message.status", nativeQuery = true)
    List<Object[]> findMsgDescList( @Param("descriptionList") List<String> descriptionList);

/**
 * GetIDType
 */
@Query(value =
"select msg.id, msg.code, msg.chn_desc from message msg, message_group grp where msg.message_group_id = grp.id and grp.DESCRIPTION in ( :descriptionList) AND msg.STATUS='A'"
        , nativeQuery = true)
List<Object[]> findMsgIdType(@Param("descriptionList") List<String> descriptionList);

/**
 * GetIDType_ZHO
 */
/*@Query(value =
"select msg.id, msg.code, msg.chn_desc from message msg, message_group grp where msg.message_group_id = grp.id and grp.id = 9 AND msg.STATUS='A'"
        , nativeQuery = true)
List<Object[]> findMsgIdType_zhuhai();*/


@Query(value =
        "select msg.id, msg.code, msg.chn_desc from message msg, message_group grp where msg.message_group_id = grp.id and grp.DESCRIPTION in ( :descriptionList) group by msg.code"
        , nativeQuery = true)
List<Object[]> findAllMsgIdType(@Param("descriptionList") List<String> descriptionList);
}

package com.macauslot.recruitment_ms.service.impl;

import com.macauslot.recruitment_ms.repository.MessageRepository;
import com.macauslot.recruitment_ms.service.IMessageService;
import com.macauslot.recruitment_ms.util.EntityUtils;
import com.macauslot.recruitment_ms.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jim.deng
 */
@Service
public class MessageServiceImpl implements IMessageService {



private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public List<MessageVO> getMsgDescCard() {
      String description =  "身份證類別";
        return findMsgDesc(description);
    }



    @Override
    public List<MessageVO> getMsgDescSource() {
        String description =  "資料來源類別";
        return findMsgDesc(description);
    }

    private List<MessageVO> findMsgDesc(String description) {
        return EntityUtils.castEntity(messageRepository.findMsgDesc(description)
                , MessageVO.class);
    }





}

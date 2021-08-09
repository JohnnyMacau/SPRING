package com.macauslot.recruitment_ms.service;

import com.macauslot.recruitment_ms.vo.MessageVO;

import java.util.List;

public interface IMessageService {


    List<MessageVO> getMsgDescCard();

    List<MessageVO> getMsgDescSource();
}

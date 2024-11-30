package com.ato.pojo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTicketsDTO {
    /**
     * 改签前机票
     */
    private Long oldOrderId;

    /**
     * 改签后机票
     */
    private TicketOrderDTO ticketOrderDTO;
}

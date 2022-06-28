package com.ad.ecom.common.stub;

import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {

    private Map<ResponseType, List<String>> responses = new HashMap<>();
    private Object responseData;

    public void addResponse(ResponseType responseType, String message) {
        List<String> messages;
        if(!responses.containsKey(responseType)) {
            messages = new ArrayList<>();
            messages.add(message);
            responses.put(responseType, messages);
        } else {
            messages = responses.get(responseType);
            messages.add(message);
        }
    }

    public void clear(ResponseType responseType) { this.responses.remove(responseType); }
    public void clearAll() {
        this.responses = new HashMap<>();
    }
}

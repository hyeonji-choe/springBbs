package org.study.springbbs.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostRequest {
    private Integer id;
    private String title;
    private String content;
    private String useYn;
}

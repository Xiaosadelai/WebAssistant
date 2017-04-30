package com.pansaian.webassistant.entity;

/**
 * Created by Administrator on 2017/4/30.
 */

public class Notes {
        private int id;
        private String content;
        private String time;
        private int status;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public String  getTime() {
            return time;
        }
        public void setTime(String  time) {
            this.time = time;
        }
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
}


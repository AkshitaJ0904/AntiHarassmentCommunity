package com.example.antisoch.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeminiApiModels {

    public static class GeminiRequest {
        @SerializedName("contents")
        private List<Content> contents;

        public GeminiRequest(String prompt) {
            Part textPart = new Part(prompt);
            List<Part> parts = List.of(textPart);
            Content content = new Content(parts);
            this.contents = List.of(content);
        }

        public List<Content> getContents() {
            return contents;
        }

        public void setContents(List<Content> contents) {
            this.contents = contents;
        }
    }

    public static class Content {
        @SerializedName("parts")
        private List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        @SerializedName("text")
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class GeminiResponse {
        @SerializedName("candidates")
        private List<Candidate> candidates;

        public List<Candidate> getCandidates() {
            return candidates;
        }

        public void setCandidates(List<Candidate> candidates) {
            this.candidates = candidates;
        }
        
        public String getFirstResponseText() {
            if (candidates != null && !candidates.isEmpty() && 
                candidates.get(0).getContent() != null && 
                candidates.get(0).getContent().getParts() != null && 
                !candidates.get(0).getContent().getParts().isEmpty()) {
                return candidates.get(0).getContent().getParts().get(0).getText();
            }
            return "Sorry, I couldn't process that request.";
        }
    }

    public static class Candidate {
        @SerializedName("content")
        private Content content;
        
        @SerializedName("finishReason")
        private String finishReason;
        
        @SerializedName("index")
        private int index;

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
} 
package com.luigi.desafiocaju.application.dto;

public record TransactionOutDTO(Boolean checkAuth, String code, String message){

        public static TransactionOutDTO approved() {
            return new TransactionOutDTO(Boolean.TRUE, "00", "APPROVED TRANSACTION");
        }

        public static TransactionOutDTO rejected() {
            return new TransactionOutDTO(Boolean.FALSE, "51", "INSUFFICIENT BALANCE TRANSACTION REJECTED");
        }

        public static TransactionOutDTO failed(){
            return new TransactionOutDTO(Boolean.FALSE, "07", "UNEXPECTED ERROR TRANSACTION FAILED");
        }

}


package com.luigi.desafiocaju.application.controller;


import com.luigi.desafiocaju.application.dto.TransactionInDTO;
import com.luigi.desafiocaju.application.dto.TransactionOutDTO;
import com.luigi.desafiocaju.application.service.auth.AuthTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    final AuthTransaction authTransaction;

    public TransactionController(AuthTransaction authTransaction) {
        this.authTransaction = authTransaction;
    }


    @Operation(summary = "Authorizes a Transaction", description = "Authorizes a transaction via MCC for a specific category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TRANSACTION SUCCESFULLY PROCESSED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "INVALID DATA",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR",
                    content = @Content
            )
    })
    @PostMapping("/authTransaction")
    public ResponseEntity<TransactionOutDTO> authTransaction(@RequestBody TransactionInDTO inDTO){
        TransactionOutDTO transactionOutDTO = authTransaction.execute(inDTO);
        return new ResponseEntity<>(transactionOutDTO, HttpStatus.OK);

    }

}

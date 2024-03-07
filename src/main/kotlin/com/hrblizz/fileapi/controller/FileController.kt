package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.dto.MetaDataRequest
import com.hrblizz.fileapi.rest.ResponseEntity
import com.hrblizz.fileapi.service.FileService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class FileController (
    private val fileService: FileService
) {
    @PostMapping("/files", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun upload(@RequestParam name: String,
               @RequestParam contentType: String,
               @RequestParam meta: String,
               @RequestParam source: String,
               @RequestParam(required = false) expireTime: String?,
               @RequestParam content: MultipartFile
    ): ResponseEntity<Map<String, Any>> {
        val token = fileService.save(name, contentType, meta, source, expireTime, content)
        return ResponseEntity(
            mapOf(
                "token" to token
            ),
            HttpStatus.CREATED.value()
        )
    }

    @PostMapping("/files/metas")
    fun getMetaData(@RequestBody request: MetaDataRequest): ResponseEntity<Map<String, Any>> {
        val fileMetadataList = fileService.getFileMetaData(request.tokens)
        return ResponseEntity(
        mapOf(
            "files" to fileMetadataList
        ),
        HttpStatus.OK.value()
        )
    }

    @GetMapping("/file/{token}")
    fun download(@PathVariable token: String): org.springframework.http.ResponseEntity<ByteArrayResource> {
        val data = fileService.get(token)
        val headers = HttpHeaders()
        headers.set("X-Filename", data.fileName)
        headers.set("X-Filesize", data.size)
        headers.set("X-CreateTime", data.createTime)
        headers.set("Content-Type", data.contentType)

        return org.springframework.http.ResponseEntity.ok().headers(headers).body(
            ByteArrayResource(data.content)
        )
    }

    @DeleteMapping("/file/{token}")
    fun delete(@PathVariable token: String): ResponseEntity<Map<String, Any>> {
        fileService.delete(token)
        return ResponseEntity(
            null,
            HttpStatus.OK.value()
        )
    }
}

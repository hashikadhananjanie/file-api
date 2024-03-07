package com.hrblizz.fileapi.data.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class FileDto {
    lateinit var token: String
    lateinit var fileName: String
    lateinit var contentType: String
    lateinit var meta: FileMetaDataDto
    lateinit var source: String
    lateinit var expireTime: String
    lateinit var size: String
    @JsonIgnore
    lateinit var content: ByteArray
    lateinit var createTime: String
}

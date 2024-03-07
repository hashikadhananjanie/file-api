package com.hrblizz.fileapi.service

import com.google.gson.Gson
import com.hrblizz.fileapi.controller.exception.BadRequestException
import com.hrblizz.fileapi.data.dto.FileDto
import com.hrblizz.fileapi.data.dto.FileMetaDataDto
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import org.apache.commons.io.IOUtils;

@Service
class FileService (
    private val template: GridFsTemplate,
    private val operations: GridFsOperations
) {
    fun save(name: String,
             contentType: String,
             meta: String,
             source: String,
             expireTime: String?,
             content: MultipartFile
    ): String {

        if (!content.isEmpty) {
            val fileMetaData = Gson().fromJson(meta, FileMetaDataDto::class.java)
            val metadata: DBObject = BasicDBObject()
            metadata.put("source", source)
            metadata.put("expireTime", expireTime)
            metadata.put("creatorEmployeeId", fileMetaData.creatorEmployeeId)
            metadata.put("size", content.size)
            metadata.put("createTime", LocalDateTime.now())

            val fileID: Any = template.store(content.inputStream, name, contentType, metadata)
            return fileID.toString()
        }
        else
            throw BadRequestException("File is not provided")
    }

    fun getFileMetaData(tokens: List<String>): List<FileDto> {

        if (tokens.isNotEmpty()) {
            val query = Query.query(Criteria.where("_id").`in`(tokens))

            return template.find(query).mapNotNull { gridFsFile ->
                val file = FileDto()
                file.meta = FileMetaDataDto()
                file.token = gridFsFile.objectId.toHexString()
                file.fileName = gridFsFile.filename
                file.size = gridFsFile.metadata["size"].toString()
                file.contentType = gridFsFile.metadata["_contentType"].toString()
                file.source = gridFsFile.metadata["source"].toString()
                file.createTime = gridFsFile.metadata["createTime"].toString()
                file.expireTime = gridFsFile.metadata["expireTime"].toString()
                file.meta.creatorEmployeeId = gridFsFile.metadata["creatorEmployeeId"] as Long
                file
            }.ifEmpty { throw BadRequestException("Files not found. Invalid tokens provided") }
        }
        else
            throw BadRequestException("Tokens not provided")
    }

    fun get(token: String): FileDto {
        val gridFSFile = template.findOne(Query.query(Criteria.where("_id").`is`(token)))
        val fileDto = FileDto()

        if (gridFSFile != null && gridFSFile.metadata != null) {
            fileDto.fileName = gridFSFile.filename
            fileDto.contentType = gridFSFile.metadata["_contentType"].toString()
            fileDto.size = gridFSFile.metadata?.get("size").toString()
            fileDto.content = IOUtils.toByteArray(operations.getResource(gridFSFile).inputStream)
            fileDto.createTime = gridFSFile.metadata["createTime"].toString()
            return fileDto
        }
        else
            throw BadRequestException("Invalid token provided")
    }

    fun delete(token: String) {
        val query = Query.query(Criteria.where("_id").`is`(token))
        val gridFSFile = template.findOne(query)

        if (gridFSFile != null)
            template.delete(query)
        else
            throw BadRequestException("Invalid token provided")
    }
}

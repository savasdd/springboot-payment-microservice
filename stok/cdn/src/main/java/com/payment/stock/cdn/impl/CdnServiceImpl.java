package com.payment.stock.cdn.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.exception.GeneralException;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.dto.ImageDto;
import com.payment.stock.entity.dto.ImageInfoDto;
import com.payment.stock.entity.model.Image;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.repository.ImageRepository;
import com.payment.stock.repository.StockRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CdnServiceImpl implements CdnService {
    private final Bucket bucket;
    private final StockRepository stockRepository;
    private final ImageRepository imageRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse fileUpload(Long stockId, MultipartFile file) {
        try {
            Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("Stock not found"));
            byte[] fileData = file.getBytes();
            String fileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0].toUpperCase() + "_" + getNumber() + getFileExtension(file.getOriginalFilename());
            Blob blob = bucket.create(fileName, fileData, getFileExtension(Objects.requireNonNull(file.getOriginalFilename())));

            if (!Objects.isNull(blob)) {
                ImageDto dto = ImageDto.builder().name(blob.getName()).link(blob.getMediaLink()).size(file.getSize()).build();
                saveImage(stock, blob.getBlobId().getBucket(), dto);
                log.info("File successfully uploaded to CDN: {}", fileName);
                return BaseResponse.success(dto);
            }

            return BaseResponse.error("File upload failed");
        } catch (Exception e) {
            throw new GeneralException("An error occurred while uploading data. Exception: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> getImageByte(Long stockId) {
        Image image = imageRepository.findByStock_IdAndRecordStatus(stockId, RecordStatus.ACTIVE).orElseThrow(() -> new RuntimeException("Image not found"));
        Blob blob = bucket.getStorage().get(image.getBucketName(), image.getName());

        log.info("get file successfully from CDN: {}", image.getName());
        return !Objects.isNull(blob) ? ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(image.getName()))).body(blob.getContent(Blob.BlobSourceOption.generationMatch())) : ResponseEntity.noContent().build();
    }

    //TODO Cacahe eklenecek
    @Override
    public BaseResponse getImage(Long stockId) {
        Optional<Image> image = imageRepository.findByStock_IdAndRecordStatus(stockId, RecordStatus.ACTIVE);
        if (image.isPresent()) {
            Blob blob = bucket.getStorage().get(image.get().getBucketName(), image.get().getName());
            byte[] encoded = Base64.getEncoder().encode(blob.getContent(Blob.BlobSourceOption.generationMatch()));

            log.info("get file successfully from CDN: {}", image.get().getName());
            return BaseResponse.success(ImageInfoDto.builder().stockId(stockId).name(image.get().getName()).image(new String(encoded, StandardCharsets.UTF_8)).build());
        }

        return BaseResponse.success(ImageInfoDto.builder().stockId(stockId).name("").build());
    }

    @Override
    public BaseResponse getAll() {
        return BaseResponse.success(imageRepository.getAllImage(RecordStatus.ACTIVE));
    }

    @Override
    public BaseResponse delete(Long stockId) {
        Image image = imageRepository.findByStock_IdAndRecordStatus(stockId, RecordStatus.ACTIVE).orElseThrow(() -> new RuntimeException("Image not found"));
        boolean blob = bucket.getStorage().delete(image.getBucketName(), image.getName());

        if (blob) {
            image.setRecordStatus(RecordStatus.DELETED);
            imageRepository.delete(image);
        }

        log.info("delete file successfully from CDN: {}", image.getName());
        return BaseResponse.success("Delete file successfully from CDN: " + blob);
    }

    @Override
    public BaseResponse getAllLoad(DataLoad load) {
        BaseLoadResponse response = imageRepository.load(load);
        return BaseResponse.success(beanUtil.mapAll(response.getData(), Image.class, ImageDto.class), response.getTotalCount());
    }

    private void saveImage(Stock stock, String bucketName, ImageDto dto) {
        Optional<Image> imageOpt = imageRepository.findByStock_IdAndRecordStatus(stock.getId(), RecordStatus.ACTIVE);
        if (imageOpt.isPresent()) {
            imageOpt.get().setRecordStatus(RecordStatus.DELETED);
            imageRepository.save(imageOpt.get());
        }

        Image image = beanUtil.mapDto(dto, Image.class);
        image.setStock(stock);
        image.setBucketName(bucketName);
        imageRepository.save(image);
    }

    private String getFileExtension(String fileName) throws Exception {
        if (fileName != null && fileName.contains(".")) {
            String[] extensionList = {".png", ".jpeg", ".jpg", ".gif"};

            for (String extension : extensionList) {
                if (fileName.endsWith(extension)) {
                    return extension;
                }
            }
        }
        throw new Exception("Not Found file type");
    }

    private BigInteger getNumber() {
        return new BigInteger(UUID.randomUUID().toString().replace("-", "").substring(0, 8), 16);
    }
}

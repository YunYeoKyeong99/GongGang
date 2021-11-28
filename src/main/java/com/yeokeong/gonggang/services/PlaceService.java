package com.yeokeong.gonggang.services;


import com.yeokeong.gonggang.model.entity.PlacePicture;
import com.yeokeong.gonggang.model.req.ReqLink;
import com.yeokeong.gonggang.model.req.ReqPlaceCreate;
import com.yeokeong.gonggang.model.req.ReqPlaceList;
import org.springframework.util.CollectionUtils;
import com.yeokeong.gonggang.httpException.ResponseError;
import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.Post;
import com.yeokeong.gonggang.model.req.ReqPostList;
import com.yeokeong.gonggang.repository.PlacePictureRepository;
import com.yeokeong.gonggang.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlacePictureRepository placePictureRepository;

    private final Environment env;
    private String imageRootPath;

    @PostConstruct
    public void postConstruct() {
        imageRootPath = env.getProperty("path.image-folder");
    }

    public List<Place> getPlaceList(Long userSeq, ReqPlaceList req){
        return placeRepository.findAllByPlaceSeqLowerThan(
                userSeq,
                req.getPageSize(),
                req.getPrevLastPostSeq()
        );
    }

    public Place getPlace(Long userSeq, Long seq) {
        Place place = placeRepository.findPlaceInfoBySeq(seq, userSeq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        return place;
    }

    public void createPlace(Long userSeq, ReqPlaceCreate req) {

        final Place place = Place.builder()
                .name(req.getName())
                .location(req.getLocation())
                .operatingTime(req.getOperatingTime())
                .menu(req.getMenu())
                .links(req.getLinks().stream()
                        .map(ReqLink::toLink)
                        .collect(Collectors.toList()))
                .build();

        placeRepository.save(place);
    }


    public void createPlacePictures(Long seq, List<MultipartFile> imageFiles) {

        Place place = placeRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        int startTurn = placePictureRepository.findFirstByPlaceSeqOrderByTurnDesc(seq)
                .map(p -> p.getTurn() + 1)
                .orElse(1);

        createPlacePictures(place, startTurn, imageFiles);
    }

    private void createPlacePictures(Place place, int startTurn, List<MultipartFile> imageFiles) {

        if (CollectionUtils.isEmpty(imageFiles)) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        String path = "/" + currentDate;
        File file = new File(imageRootPath + path);

        if (!file.exists()) {
            file.mkdirs();
        }

        final Tika tika = new Tika();
        int turn = startTurn;

        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);
            String mimeTypeString = null;
            try {
                mimeTypeString = tika.detect(imageFile.getInputStream());
            } catch (Exception e) {
                throw ResponseError.BadRequest.INVALID_IMAGE_TYPE.getResponseException();
            }

            String originalFileExtension;
            if (MediaType.IMAGE_PNG_VALUE.equals(mimeTypeString)) {
                originalFileExtension = ".png";
            } else if (MediaType.IMAGE_JPEG_VALUE.equals(mimeTypeString)) {
                originalFileExtension = ".jpg";
            } else {
                throw ResponseError.BadRequest.INVALID_IMAGE_TYPE.getResponseException();
            }

            String newFileName = System.nanoTime() + originalFileExtension;
            file = new File(imageRootPath + path + "/" + newFileName);
            try {
                imageFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw ResponseError.InternalServerError.FAIL_TO_UPLOAD_IMAGE.getResponseException();
            }

            PlacePicture placePicture = PlacePicture.builder()
                    .place(place)
                    .path(path + "/" + newFileName)
                    .turn(turn + i)
                    .build();

            placePictureRepository.save(placePicture);

        }
    }

}

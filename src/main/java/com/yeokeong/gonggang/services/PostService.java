package com.yeokeong.gonggang.services;

import com.yeokeong.gonggang.common.PostStatus;
import com.yeokeong.gonggang.httpException.ResponseError;
import com.yeokeong.gonggang.model.entity.*;
import com.yeokeong.gonggang.model.req.ReqPostCreate;
import com.yeokeong.gonggang.model.req.ReqPostList;
import com.yeokeong.gonggang.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostPictureRepository postPictureRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostCategoryRepository postCategoryRepository;

    private final Environment env;
    private String imageRootPath;

    @PostConstruct
    public void postConstruct() {
        imageRootPath = env.getProperty("path.image-folder");
    }

    public List<Post> getBestPostList(Long userSeq) {
        return postRepository.findAll(); // FIXME
    }

    public List<Post> getRecommandPostList(Long userSeq) {
        return postRepository.findAll(); // FIXME
    }

    public List<Post> getBookmarkPostList(Long userSeq, ReqPostList req) {
        return postRepository.findAllByPostBookMark(
                userSeq,
                req.getPageSize(),
                req.getPrevLastPostSeq()

        );
    }

    public List<Post> getMyPostList(Long userSeq, ReqPostList req) {
        return postRepository.findAllByUserSeq(
                userSeq,
                req.getPageSize(),
                req.getPrevLastPostSeq()

        );
    }

    public List<Post> getPostList(Long userSeq, ReqPostList req) {
        return postRepository.findAll(
                userSeq,
                req.getTimingType(),
                req.getCostType(),
                req.getDurationTimeType(),
                req.getPageSize(),
                req.getPrevLastPostSeq()
        );
    }

    public Post getPost(Long userSeq, Long seq) {
        Post post = postRepository.findPostInfoBySeq(seq, userSeq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

//        boolean isLike = postLikeRepository.countByPostSeqAndUserSeq(seq, userSeq) == 1;
//        post.setIsLike(isLike);

//        boolean isBookmark = postBookmarkRepository.countByPostSeqAndUserSeq(seq,userSeq) == 1;
//        post.setIsBookmark(isBookmark);

        return post;
    }

    public void createPost(Long userSeq, ReqPostCreate req) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        final Post post = Post.builder()
                .user(user)
                .title(req.getTitle())
                .content(req.getContent())
                .timingType(req.getTimingType())
                .durationTimeType(req.getDurationTimeType())
                .costType(req.getCostType())
                .status(PostStatus.NORMAL)
                .likeCnt(0L)
                .linkUrl(req.getLinkUrl())
                .build();

        postRepository.save(post);

        /*
        INSERT INTO post_category(category_name, post_seq)
        VALUES
        (~~~~~~~1~~~~~~~~),
        (~~~~~~~2~~~~~~~~),
        (~~~~~~~3~~~~~~~~);
         */
        postCategoryRepository.saveAll(req.getCategoryTypeList()
                .stream()
                .map(type -> PostCategory.builder()
                        .categoryType(type)
                        .postSeq(post.getSeq())
                        .build())
                .collect(Collectors.toList()));
    }

    public void createPostPictures(Long userSeq, Long seq, List<MultipartFile> imageFiles) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        int startTurn = postPictureRepository.findFirstByPostSeqOrderByTurnDesc(seq)
                .map(p -> p.getTurn() + 1)
                .orElse(1);

        createPostPictures(post, startTurn, imageFiles);
    }

    public void deletePost(Long userSeq, Long seq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        postRepository.delete(post);
    }

    public void likePost(Long userSeq, Long seq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        PostLike postLike = PostLike.builder()
                .postSeq(seq)
                .userSeq(userSeq)
                .build();

        try {
            postLikeRepository.save(postLike);
        } catch (Exception e) { }

        postRepository.incrementLikeCnt(post.getSeq());
    }

    public void unlikePost(Long userSeq, Long seq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        PostLike postLike = PostLike.builder()
                .postSeq(seq)
                .userSeq(userSeq)
                .build();

        try {
            postLikeRepository.delete(postLike);
        } catch (Exception e) { }

        postRepository.decrementLikeCnt(post.getSeq());
    }

    public void createPostBookmark(Long userSeq, Long seq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        PostBookmark postBookmark = PostBookmark.builder()
                .postSeq(seq)
                .userSeq(userSeq)
                .build();

        try {
            postBookmarkRepository.save(postBookmark);
        } catch (Exception e) { }
    }

    public void deletePostBookmark(Long userSeq, Long seq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Post post = postRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POST_NOT_EXISTS::getResponseException);

        if(!post.getUser().getSeq().equals(user.getSeq())) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        PostBookmark postBookmark = PostBookmark.builder()
                .postSeq(seq)
                .userSeq(userSeq)
                .build();

        try {
            postBookmarkRepository.delete(postBookmark);
        } catch (Exception e) { }
    }

    private void createPostPictures(Post post, int startTurn, List<MultipartFile> imageFiles) {

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

        for(int i=0 ; i<imageFiles.size() ; i++) {
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

            PostPicture postPicture = PostPicture.builder()
                    .post(post)
                    .path(path + "/" + newFileName)
                    .turn(turn + i)
                    .build();

            postPictureRepository.save(postPicture);
        }
    }
}

package org.cloud.userservice.utils;

import com.cloud.common.pojo.ShareMsg;
import com.cloud.common.pojo.file.Share;
import com.cloud.common.pojo.file.Status;

public class ShareParse {
    public static ShareMsg getShareMsg(Share share) {
        ShareMsg shareMsg = new ShareMsg();
        shareMsg.setId(share.getId());
        shareMsg.setUser_name(share.getUser().getUsername());
        shareMsg.setFile_name(share.getUserFile().getFile_name());
        shareMsg.setFile_size(share.getUserFile().getSize());
        shareMsg.setStatus(share.getStatus());
        shareMsg.setInvalid_time(share.getInvalid_time());
        return shareMsg;
    }
}

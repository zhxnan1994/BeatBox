package com.zhang.shaon.beatbox;

/**
 * Created by zhang on 2017-11-19.
 */

public class Sound {
    private String mAssetPath;
    private String mName;

    public Sound (String assetPath){
        mAssetPath=assetPath;
        String[] components = assetPath.split("/");
        String fileName = components[components.length - 1];
        mName=fileName.replace("wav","");
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }
}

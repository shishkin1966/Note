package com.cleanarchitecture.sl.repository;

import com.cleanarchitecture.sl.event.ui.ImageEvent;

/**
 * Created by Shishkin on 22.12.2017.
 */

public interface INetImageProvider extends Provider {

    /**
     * Скачать image по его url
     *
     * @param event - событие для скачивания image
     */
    void downloadImage(ImageEvent event);
}

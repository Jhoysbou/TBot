package com.jhoysbou.TBot.utils;

import java.util.Optional;

public interface OpenLinkExtractor {
    Optional<String> extract(final String text);

}

package com.jhoysbou.TBot.utils;

import java.util.Optional;

public interface HashtagParser {
    Optional<String> parse(final Optional<String> text);
}

package com.example.magictextspan

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/4/10 16:42
 * Description: com.example.magictextspan
 */
enum class SpannedType {
    /**
     * If you aren't modifying the text or markup after creation, use SpannedString.
     */
    SPANNED_STRING,

    /**
     * If you need to attach a small number of spans to a single text object, and the text itself is read-only, use SpannableString.
     */
    SPANNABLE_STRING,

    /**
     * If you need to modify text after creation, and you need to attach spans to the text, use SpannableStringBuilder.
     * If you need to attach a large number of spans to a text object, regardless of whether the text itself is read-only, use SpannableStringBuilder.
     */
    SPANNABLE_STRING_BUILDER
}
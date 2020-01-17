package app.perdana.indonesia.core.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import app.perdana.indonesia.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import id.zelory.compressor.Compressor
import java.io.File

fun ImageView.loadWithGlidePlaceholder(
    imageUrl: String,
    placeHolderImage: Int = R.drawable.no_image,
    roundedCorner: Int = 1
) {
    val glideOption = RequestOptions()
        .placeholder(placeHolderImage)
        .error(placeHolderImage)
        .transform(RoundedCorners(roundedCorner))

    Glide.with(this.context)
        .applyDefaultRequestOptions(glideOption)
        .load(imageUrl)
        .into(this)
}

fun ImageView.loadWithGlidePlaceholder(
    imageUri: Uri?,
    placeHolderImage: Int = R.drawable.no_image,
    roundedCorner: Int = 1
) {
    val glideOption = RequestOptions()
        .placeholder(placeHolderImage)
        .error(placeHolderImage)
        .transform(RoundedCorners(roundedCorner))

    Glide.with(this.context)
        .applyDefaultRequestOptions(glideOption)
        .load(imageUri)
        .into(this)
}

fun ImageView.loadWithGlidePlaceholder(
    imageFile: File?,
    placeHolderImage: Int = R.drawable.no_image,
    roundedCorner: Int = 1
) {
    val glideOption = RequestOptions()
        .placeholder(placeHolderImage)
        .error(placeHolderImage)
        .transform(RoundedCorners(roundedCorner))

    Glide.with(this.context)
        .applyDefaultRequestOptions(glideOption)
        .load(imageFile)
        .into(this)
}

fun ImageView.loadWithGlidePlaceholder(
    resourceId: Int?,
//    placeHolderImage: Int = R.drawable.no_image,
    roundedCorner: Int = 1
) {
    val glideOption = RequestOptions()
//        .placeholder(placeHolderImage)
//        .error(placeHolderImage)
        .transform(RoundedCorners(roundedCorner))
    Glide.with(this.context)
        .applyDefaultRequestOptions(glideOption)
        .load(resourceId)
        .into(this)
}

fun ImageView.loadWithGlidePlaceholder(
    imageDrawable: Drawable,
    placeHolderImage: Int = R.drawable.no_image,
    roundedCorner: Int = 1
) {
    val glideOption = RequestOptions()
        .placeholder(placeHolderImage)
        .error(placeHolderImage)
        .transform(RoundedCorners(roundedCorner))

    Glide.with(this.context)
        .applyDefaultRequestOptions(glideOption)
        .load(imageDrawable)
        .into(this)
}

fun File.compress(context: Context, quality: Int = 30): File =
    Compressor(context).setQuality(quality)
        .setCompressFormat(Bitmap.CompressFormat.JPEG)
        .compressToFile(this)



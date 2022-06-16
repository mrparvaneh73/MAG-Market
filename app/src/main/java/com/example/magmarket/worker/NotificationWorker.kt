package com.example.magmarket.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.magmarket.R
import com.example.magmarket.application.Constants
import com.example.magmarket.data.datastore.newestproduct.NewestProduct
import com.example.magmarket.data.datastore.newestproduct.NewestProductDataStore
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.ui.mainactivity.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val productRepository: ProductRepository,
    private val newestProductDataStore: NewestProductDataStore
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        insertLastProductTolocal()

        try {
            newestProductDataStore.getLastProduct().collect {ListOfnewestProduct ->

//                if (ListOfnewestProduct.size > 1) {
//                    notificationBuilder(
//                        productName = it[it.lastIndex].name,
//                        productId = it[it.lastIndex].id.toString()
//                    )
//                    productRepository.deleteLastPreviewsProduct(it.first())
//                }
            }
            return Result.success()
        } catch (error: Throwable) {
            return Result.failure()
        }
    }

    private suspend fun insertLastProductTolocal() {
        productRepository.getSortedProducts().collect {
            newestProductDataStore.insertNewestProduct(
                NewestProduct(
                    it[0].id.toInt(),
                    name = it[0].name!!
                )
            )
        }
    }

    private fun notificationBuilder(productName: String, productId: String) {
        val bundle = Bundle()
        bundle.putString("productId", productId)
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.productDetailFragment)
            .setArguments(bundle)
            .createPendingIntent()
//        val nextIntent = NavDeepLinkBuilder(context)
//            .setComponentName(MainActivity::class.java)
//            .setGraph(R.navigation.navigation)
//            .setDestination(R.id.homeFragment)
//            .setArguments(bundleOf("productId" to productId))
//            .createPendingIntent()

//
//        val stackBuilder = TaskStackBuilder.create(
//            context
//        )
//        stackBuilder.addNextIntentWithParentStack(nextIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                Constants.CHANNEL_ID_1, Constants.CHANNEL_NAME, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID_1)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("NEW PRODUCT")
            .setContentText(productName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_ALL)



        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setAutoCancel(true)
        notificationManager.notify(50, mBuilder.build())
    }
}
/*******************************************************************************
    Copyright(c) 2008 - 2015 Pinecone Electronics
    All Rights Reserved. By using this module you agree to the terms of the
    Pinecone Electronics License Agreement for it.
********************************************************************************
* Filename	: EntryReceiver.java
*
* Description	: <describing what this file is to do>
*
* Notes		: <the limitations to use this file>
*
*--------------------------------------------------------------------------------
* Change History: 
*--------------------------------------------------------------------------------
*          
* 2016-01-29, AUTHOR, create originally.
*
*******************************************************************************/

package com.pinecone.d2dinfofloatball;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class D2dInfoBootReceiver extends BroadcastReceiver {

    private static final String TAG = "D2dInfoBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        switch(intent.getAction()){
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d(TAG,"start D2DInfoFBService");
                Intent D2dInfoFBServiceBoot = new Intent(context, D2DInfoFBService.class);
                context.startService(D2dInfoFBServiceBoot);
                break;

            default:break;

        }




    }
}


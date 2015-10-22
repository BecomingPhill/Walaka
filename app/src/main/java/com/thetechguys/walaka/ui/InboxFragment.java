package com.thetechguys.walaka.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thetechguys.walaka.adapters.MessageAdapter;
import com.thetechguys.walaka.utils.ParseConstants;
import com.thetechguys.walaka.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheBank on 19/9/14.
 */
public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh1,R.color.swipeRefresh2,R.color.swipeRefresh3,R.color.swipeRefresh4);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);

        retriveMessages();

    }

    private void retriveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (mSwipeRefreshLayout.isRefreshing()){mSwipeRefreshLayout.setRefreshing(false);}

                if (e == null) {
                    mMessages = messages;

                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;


                    }
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adapter);
                    } else {

                        //refill adapter
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);

                    }

                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
            //View Image
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        } else {
            //View Video
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);
        }
        //delete message
        //deleteMessage(message);

    }

    private void deleteMessage(ParseObject message) {
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

        if (ids.size()== 1){
            //last recipient, delete from server
            message.deleteInBackground();

        }
          else{
            //remove recipient, save
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());


            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();

        }
    }
    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retriveMessages();

        }
    };


}


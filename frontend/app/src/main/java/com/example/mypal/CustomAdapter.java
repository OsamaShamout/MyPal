package com.example.mypal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private String[] localDataSet;
        private List<Card> mCards;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView activity_name;
            private final TextView activity_date;
            private final ImageView activity_image;


            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                activity_name = (TextView) view.findViewById(R.id.activityName11);
                activity_date = (TextView) view.findViewById(R.id.activityDate11);
                activity_image = (ImageView) view.findViewById(R.id.activityImage11);
            }

            public TextView getActivity_Name() {
                return activity_name;
            }

            public TextView getActivity_Date() {
                return activity_date;
            }

            public ImageView getActivity_image() {
                return activity_image;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public CustomAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        public CustomAdapter(List<Card> cards) {
            mCards = cards;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_look, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getActivity_Name().setText(localDataSet[position]);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }


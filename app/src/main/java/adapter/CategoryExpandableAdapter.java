package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectgithub.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.models.AutoMotiveCategorySubcategory.AutoMotiveData;
import model.models.AutoMotiveCategorySubcategory.Subcategory;

public class CategoryExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AutoMotiveData> categorysubcategorydatas;
    private Animation animation;

    public CategoryExpandableAdapter(Context context, List<AutoMotiveData> categorysubcategorydata) {
        this.context = context;
        this.categorysubcategorydatas = categorysubcategorydata;
    }

    //checked
    @Override
    public int getGroupCount() {
        return categorysubcategorydatas.size();
    }

    //checked
    @Override
    public int getChildrenCount(int groupposition) {
        return categorysubcategorydatas.get(groupposition).getSubcategory().size();
    }

    //checked
    @Override
    public Object getGroup(int groupPosition) {
        return categorysubcategorydatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categorysubcategorydatas.get(groupPosition).getSubcategory().get(childPosition);
    }

    //checked
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //checked
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AutoMotiveData autoMotiveData=(AutoMotiveData) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.childadapter, null);
        }
             ImageView grouparrow=convertView.findViewById(R.id.grouparrow);
             ImageView categorysubcategoryimage=convertView.findViewById(R.id.categorysubcategoryimage);
             TextView categorysubcategory=convertView.findViewById(R.id.categorysubcategory);
            categorysubcategory.setText(autoMotiveData.getCategory());

            categorysubcategory.setTypeface(Typeface.DEFAULT_BOLD);
            categorysubcategory.setTextColor(context.getResources().getColor(R.color.white));

//            Picasso.with(context)
////                    .load(ConstantUrl.IMAGE_URL +autoMotiveData.getCategoryImage())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE )
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
////                    .error(R.drawable.noimage)
////                    .placeholder(R.drawable.progressloadimage)
//                    .into(categorysubcategoryimage);

            if(isExpanded)
                grouparrow.animate().rotation(180).setInterpolator(new LinearInterpolator()).setDuration(500);
            else
                grouparrow.animate().rotation(0).setInterpolator(new LinearInterpolator()).setDuration(500);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Subcategory subcategory=(Subcategory) getChild(groupPosition,childPosition);
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childadapter, null);

        }
            ImageView grouparrow=convertView.findViewById(R.id.grouparrow);
            ImageView categorysubcategoryimage=convertView.findViewById(R.id.categorysubcategoryimage);
            TextView categorysubcategory=convertView.findViewById(R.id.categorysubcategory);
//            categorysubcategory.setText(categorysubcategorydatas.get(groupPosition).getSubcategory().get(childPosition).getSubcategory());
            categorysubcategory.setText(subcategory.getSubcategory());
            RelativeLayout expandableadapter=convertView.findViewById(R.id.expandableadapter);
            grouparrow.setVisibility(View.GONE);
            expandableadapter.setBackgroundColor(context.getResources().getColor(R.color.white));
//            Picasso.with(context)
//                    .load(ConstantUrl.IMAGE_URL+subcategory.getSubcategoryimage())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE )
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .error(R.drawable.noimage)
//                    .placeholder(R.drawable.progressloadimage)
//                    .into(categorysubcategoryimage);

            expandableadapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Bundle categorysubcategory=new Bundle();
//                    categorysubcategory.putString("category",categorysubcategorydatas.get(groupPosition).getCategory());
//                    categorysubcategory.putString("subcategory",categorysubcategorydatas.get(groupPosition).getSubcategory().get(childPosition).getSubcategory());
//                    FragmentManager fragmentManager=activity.getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    BrandFragment brandFragment=new BrandFragment();
//                    fragmentManager.beginTransaction();
//                    brandFragment.setArguments(categorysubcategory);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.replace(R.id.fragmentlayout,brandFragment).addToBackStack(null).commit();
                }
            });

//        animation = AnimationUtils.loadAnimation(context, R.anim.scale);
//        convertView.startAnimation(animation);
        return convertView;
    }
    //checked
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

package com.hupu.app.android.smartcourt.view.city_list;

import android.content.Context;
import android.text.TextUtils;
import android.widget.SectionIndexer;

import com.ht.framework.utils.Log;
import com.ht.framework.widget.htrecyclerview.BaseRecyclerViewAdapter;
import com.ht.framework.widget.htrecyclerview.RecyclerViewHolder;
import com.hupu.app.android.smartcourt.R;
import com.hupu.app.android.smartcourt.module.SportCity;

import java.util.List;

/**
 * 城市列表适配器
 * <p>
 * Created by chengwangqi on 2015/3/20.
 */
public class CityListAdapter extends BaseRecyclerViewAdapter<SportCity> implements SectionIndexer {

    private long currentCityId = -10;

    private String mSections = "";

    public CityListAdapter(Context context, List<SportCity> items, int layoutId) {
        super(context, items, layoutId);
        initSections();
    }

    public CityListAdapter(Context context, List<SportCity> items, int layoutId, int headerLayoutId) {
        super(context, items, layoutId, headerLayoutId);
        initSections();
    }

    private void initSections() {
        mSections += "#";
        for (int i = 0; i < items.size(); i++) {
            String pinyin = items.get(i).getPinyin();
            if (!TextUtils.isEmpty(pinyin) && !pinyin.contains("#") && !pinyin.contains("*")) {
                String firstLetter = String.valueOf(pinyin.charAt(0)).toUpperCase();
                if (!mSections.contains(firstLetter)) {
                    mSections += firstLetter;
                }
            }
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerViewHolder holder, final int position) {
        final SportCity city = items.get(position);
        if (city != null) {
            if (city.getId() == 0) {
                holder.setCheckedMark(android.R.id.text1, 0);
                if (isLocatedCitySupported(city)) {
                    holder.setText(android.R.id.text1, city.getName());

                } else {
                    String cityName;
                    if (city.getName() == null || (city.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.located_fail)) ||
                            city.getName().isEmpty())) {
                        cityName = mContext.getResources().getString(R.string.located_fail);
                    } else {
                        cityName = city.getName() + "(暂未支持)";
                    }
                    holder.setText(android.R.id.text1, cityName);
                }
            } else {
                holder.setText(android.R.id.text1, city.getName());

                if (currentCityId == city.getId() && position != 0) {
                    holder.setCheckedMark(android.R.id.text1, R.drawable.ic_confirm);
                } else {
                    holder.setCheckedMark(android.R.id.text1, 0);
                }
            }
        }
    }

    private boolean isLocatedCitySupported(SportCity locatedCity) {
        String locatedCityName = locatedCity.getName();
        if (locatedCityName == null) {
            locatedCity.setId(-1);
            return false;
        }
        for (int idx = 1; idx < items.size(); idx++) {
            if (locatedCityName.contains(items.get(idx).getName())) {
                locatedCity.setId(items.get(idx).getId());
                locatedCity.setName(items.get(idx).getName());
                return true;
            }
        }
        locatedCity.setId(-1);
        return false;
    }

    public void setCurrentCityId(long currentCityId) {
        this.currentCityId = currentCityId;
    }

    public void setCities(List<SportCity> cities) {
        this.items = cities;
        this.notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int position) {
        return String.valueOf(getItem(position).getPinyin().charAt(0)).toUpperCase().hashCode();
    }

    @Override
    public long generateHeaderId(int position) {
        return String.valueOf(getItem(position).getPinyin().charAt(0)).toUpperCase().hashCode();
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerViewHolder holder, int position) {
        if (getItem(position).getPinyin().charAt(0) == '#') {
            holder.setText(R.id.header_pre_city_name, "定位城市");
        } else if (getItem(position).getPinyin().charAt(0) == '*') {
            holder.setText(R.id.header_pre_city_name, "热门城市");
        } else {
            holder.setText(R.id.header_pre_city_name, String.valueOf(getItem(position).getPinyin().charAt(0)).toUpperCase());
        }
    }


    @Override
    public Object[] getSections() {
        String[] secArry = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            secArry[i] = String.valueOf(mSections.charAt(i));
        return secArry;
    }


    @Override
    public int getPositionForSection(int sectionIndex) {
        String sectionChar = String.valueOf(mSections.charAt(sectionIndex));
        for (int i = 0; i < getItemCount(); i++) {
            String itemFirstChar = String.valueOf(getItem(i).getPinyin().charAt(0)).toUpperCase();
            if (sectionChar.equalsIgnoreCase(itemFirstChar)) {
                Log.d("index","index:" + sectionIndex + ",pos:" + i + ",pinyin:" + getItem(i).getPinyin());
                return i;
            }
        }
        Log.d("index","index:" + sectionIndex + ",pos:" + 0 + ",pinyin:" + getItem(0).getPinyin());
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
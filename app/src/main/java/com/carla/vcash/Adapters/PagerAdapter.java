package com.carla.vcash.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.carla.vcash.Fragments.HomeFragment;
import com.carla.vcash.Fragments.PaymentsFragment;
import com.carla.vcash.Fragments.TransferFragment;

import org.jetbrains.annotations.NotNull;

public class PagerAdapter extends FragmentPagerAdapter
{
    private int numOfTabs;
    private HomeFragment home;
    private PaymentsFragment payments;
    private TransferFragment transfer;

    public PagerAdapter(FragmentManager fragmentManager, int i)
    {
        super(fragmentManager, i);
        home = new HomeFragment();
        payments = new PaymentsFragment();
        transfer = new TransferFragment();

        this.numOfTabs = 3;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return home;
            case 1:
                return payments;
            case 2:
                return transfer;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }
}

package org.techtown.volleyball.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import org.techtown.volleyball.R
import org.techtown.volleyball.base.BaseFragment
import org.techtown.volleyball.databinding.FragmentTeamNewsBinding

class TeamNewsFragment : BaseFragment<FragmentTeamNewsBinding>(){
    override val layoutResID: Int
        get() = R.layout.fragment_team_news

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")


    }
}
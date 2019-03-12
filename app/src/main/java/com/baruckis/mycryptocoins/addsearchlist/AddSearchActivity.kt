/*
 * Copyright 2018 Andrius Baruckis www.baruckis.com | mycryptocoins.baruckis.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baruckis.mycryptocoins.addsearchlist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.baruckis.mycryptocoins.R
import com.baruckis.mycryptocoins.data.Cryptocurrency
import com.baruckis.mycryptocoins.dependencyinjection.Injectable
import kotlinx.android.synthetic.main.activity_add_search.*
import javax.inject.Inject


class AddSearchActivity : AppCompatActivity(), Injectable {

    private lateinit var listView: ListView
    private lateinit var listAdapter: AddSearchListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AddSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_search)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.listview_activity_add_search)

        setupList()
        subscribeUi()
    }

    private fun setupList() {
        listAdapter = AddSearchListAdapter(this)
        listView.adapter = listAdapter
    }

    private fun subscribeUi() {

        // Obtain ViewModel from ViewModelProviders, using parent activity as LifecycleOwner.
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddSearchViewModel::class.java)

        // Update the list when the data changes by observing data on the ViewModel, exposed as a LiveData.
        viewModel.liveData.observe(this, Observer<List<Cryptocurrency>> { data ->
            if (data != null && data.isNotEmpty()) {
                listView.visibility = View.VISIBLE
                listAdapter.setData(data)
            } else {
                listView.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE // expand to full width, to have close button set to the right side.

        return true
    }
}
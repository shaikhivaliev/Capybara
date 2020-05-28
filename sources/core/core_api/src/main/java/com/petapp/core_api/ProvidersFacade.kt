package com.petapp.core_api

import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import com.petapp.core_api.navigation.FragmentProvider
import com.petapp.core_api.navigation.NavigationProvider

interface ProvidersFacade : AppProvider, DatabaseProvider, NavigationProvider, FragmentProvider
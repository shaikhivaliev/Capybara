package com.petapp.core_api

import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider

interface ProvidersFacade : DatabaseProvider, AppProvider
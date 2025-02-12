package com.breezefieldsalesdemo.app

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.Context
import com.breezedsm.app.domain.NewOrderDataDao
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezedsm.app.domain.NewProductListDao
import com.breezedsm.app.domain.NewProductListEntity
import com.breezedsm.app.domain.NewRateListDao
import com.breezedsm.app.domain.NewRateListEntity

import com.breezefieldsalesdemo.app.AppConstant.DBNAME
import com.breezefieldsalesdemo.app.domain.*
import com.breezefieldsalesdemo.features.lead.model.LeadActivityDao
import com.breezefieldsalesdemo.features.lead.model.LeadActivityEntity
import com.breezefieldsalesdemo.features.location.UserLocationDataDao
import com.breezefieldsalesdemo.features.location.UserLocationDataEntity
import com.breezefieldsalesdemo.features.login.*
import com.breezefieldsalesdemo.features.taskManagement.model.TaskManagementDao
import com.breezefieldsalesdemo.features.taskManagement.model.TaskManagmentEntity


/*
 * Copyright (C) 2017 Naresh Gowd Idiga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Revision History
// 1.0   AppV 4.0.6  Saheli    05/012/2023 shop_extra_contact migration
// 2.0   AppV 4.0.6  Saheli    06/01/2023 shop_activity and tbl_shop_deefback migration
// 3.0   AppV 4.0.6  Saheli    11/01/2023  shopStatusUpdate migration
// 4.0   AppV 4.0.6  Saheli    20/01/2023  order_product_list order_mrp & order_discount  migration mantis 25601
// 5.0   AppV 4.0.6  Saheli    01/02/2023  product_list   migration changes

@Database(entities = arrayOf(AddShopDBModelEntity::class, UserLocationDataEntity::class, UserLoginDataEntity::class, ShopActivityEntity::class,
        StateListEntity::class, CityListEntity::class, MarketingDetailEntity::class, MarketingDetailImageEntity::class, MarketingCategoryMasterEntity::class,
        TaListDBModelEntity::class, AssignToPPEntity::class, AssignToDDEntity::class, WorkTypeEntity::class, OrderListEntity::class,
        OrderDetailsListEntity::class, ShopVisitImageModelEntity::class, UpdateStockEntity::class, PerformanceEntity::class,
        GpsStatusEntity::class, CollectionDetailsEntity::class, InaccurateLocationDataEntity::class, LeaveTypeEntity::class, RouteEntity::class,
        ProductListEntity::class, OrderProductListEntity::class, StockListEntity::class, RouteShopListEntity::class, SelectedWorkTypeEntity::class,
        SelectedRouteEntity::class, SelectedRouteShopListEntity::class, OutstandingListEntity::class/*, LocationEntity::class*/,
        IdleLocEntity::class, BillingEntity::class, StockDetailsListEntity::class, StockProductListEntity::class, BillingProductListEntity::class,
        MeetingEntity::class, MeetingTypeEntity::class, ProductRateEntity::class, AreaListEntity::class, PjpListEntity::class,
        ShopTypeEntity::class, ModelEntity::class, PrimaryAppEntity::class, SecondaryAppEntity::class, LeadTypeEntity::class,
        StageEntity::class, FunnelStageEntity::class, BSListEntity::class, QuotationEntity::class, TypeListEntity::class,
        MemberEntity::class, MemberShopEntity::class, TeamAreaEntity::class, TimesheetListEntity::class, ClientListEntity::class,
        ProjectListEntity::class, ActivityListEntity::class, TimesheetProductListEntity::class, ShopVisitAudioEntity::class,
        TaskEntity::class, BatteryNetStatusEntity::class, ActivityDropDownEntity::class, TypeEntity::class,
        PriorityListEntity::class, ActivityEntity::class, AddDoctorProductListEntity::class, AddDoctorEntity::class,
        AddChemistProductListEntity::class, AddChemistEntity::class, DocumentypeEntity::class, DocumentListEntity::class, PaymentModeEntity::class,
        EntityTypeEntity::class, PartyStatusEntity::class, RetailerEntity::class, DealerEntity::class, BeatEntity::class, AssignToShopEntity::class,
        VisitRemarksEntity::class, ShopVisitCompetetorModelEntity::class,
        OrderStatusRemarksModelEntity::class, CurrentStockEntryModelEntity::class, CurrentStockEntryProductModelEntity::class,
        CcompetetorStockEntryModelEntity::class, CompetetorStockEntryProductModelEntity::class,
        ShopTypeStockViewStatus::class,
        NewOrderGenderEntity::class, NewOrderProductEntity::class, NewOrderColorEntity::class, NewOrderSizeEntity::class, NewOrderScrOrderEntity::class, ProspectEntity::class,
        QuestionEntity::class, QuestionSubmitEntity::class, AddShopSecondaryImgEntity::class, ReturnDetailsEntity::class, ReturnProductListEntity::class, UserWiseLeaveListEntity::class, ShopFeedbackEntity::class, ShopFeedbackTempEntity::class, LeadActivityEntity::class,
        ShopDtlsTeamEntity::class, CollDtlsTeamEntity::class, BillDtlsTeamEntity::class, OrderDtlsTeamEntity::class,
        TeamAllShopDBModelEntity::class, DistWiseOrderTblEntity::class, NewGpsStatusEntity::class,ShopExtraContactEntity::class,ProductOnlineRateTempEntity::class, TaskManagmentEntity::class,
    VisitRevisitWhatsappStatus::class,CallHisEntity::class,CompanyMasterEntity::class,TypeMasterEntity::class,StatusMasterEntity::class,SourceMasterEntity::class,StageMasterEntity::class,TeamListEntity::class,
    ContactActivityEntity::class,ScheduleTemplateEntity::class,ModeTemplateEntity::class,RuleTemplateEntity::class,SchedulerMasterEntity::class,
    SchedulerDateTimeEntity::class,SchedulerContactEntity::class,TeamAllListEntity::class,PhoneContactEntity::class,PhoneContact1Entity::class,
    NewProductListEntity::class, NewRateListEntity::class, NewOrderDataEntity::class,NewOrderProductDataEntity::class,OpportunityStatusEntity::class,OpportunityAddEntity::class,OpportunityProductEntity::class,LmsUserInfoEntity::class,
    ShopAudioEntity::class,LMSNotiEntity::class,StockAllEntity::class,StockTransEntity::class),
        version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addShopEntryDao(): AddShopDao
    abstract fun userLocationDataDao(): UserLocationDataDao
    abstract fun userAttendanceDataDao(): UserAttendanceDataDao
    abstract fun shopActivityDao(): ShopActivityDao
    abstract fun stateDao(): StateListDao
    abstract fun cityDao(): CityListDao
    abstract fun marketingDetailDao(): MarketingDetailDao
    abstract fun marketingDetailImageDao(): MarketingDetailImageDao
    abstract fun marketingCategoryMasterDao(): MarketingCategoryMasterDao

    //New implementation
    abstract fun taListDao(): TaListDao

    abstract fun ppListDao(): AssignToPPDao
    abstract fun ddListDao(): AssignToDDDao
    abstract fun workTypeDao(): WorkTypeDao
    abstract fun orderListDao(): OrderListDao
    abstract fun orderDetailsListDao(): OrderDetailsListDao
    abstract fun shopVisitImageDao(): ShopVisitImageDao
    abstract fun shopVisitCompetetorImageDao(): ShopVisitCompetetorDao
    abstract fun shopVisitOrderStatusRemarksDao(): OrderStatusRemarksDao
    abstract fun shopCurrentStockEntryDao(): CurrentStockEntryDao
    abstract fun shopCurrentStockProductsEntryDao(): CurrentStockEntryProductDao
    abstract fun competetorStockEntryDao(): CompetetorStockEntryDao
    abstract fun competetorStockEntryProductDao(): CompetetorStockEntryProductDao
    abstract fun shopTypeStockViewStatusDao(): ShopTypeStockViewStatusDao
    abstract fun updateStockDao(): UpdateStockDao
    abstract fun performanceDao(): PerformanceDao
    abstract fun gpsStatusDao(): GpsStatusDao
    abstract fun collectionDetailsDao(): CollectionDetailsDao
    abstract fun inaccurateLocDao(): InAccurateLocDataDao
    abstract fun leaveTypeDao(): LeaveTypeDao
    abstract fun routeDao(): RouteDao
    abstract fun productListDao(): ProductListDao
    abstract fun orderProductListDao(): OrderProductListDao
    abstract fun stockListDao(): StockListDao
    abstract fun routeShopListDao(): RouteShopListDao
    abstract fun selectedWorkTypeDao(): SelectedWorkTypeDao
    abstract fun selectedRouteListDao(): SelectedRouteDao
    abstract fun selectedRouteShopListDao(): SelectedRouteShopListDao
    abstract fun updateOutstandingDao(): OutstandingListDao

    //abstract fun locationDao(): LocationDao
    abstract fun idleLocDao(): IdleLocDao

    abstract fun billingDao(): BillingDao
    abstract fun stockDetailsListDao(): StockDetailsListDao
    abstract fun stockProductDao(): StockProductListDao
    abstract fun billProductDao(): BillingProductListDao
    abstract fun addMeetingDao(): MeetingDao
    abstract fun addMeetingTypeDao(): MeetingTypeDao
    abstract fun productRateDao(): ProductRateDao
    abstract fun areaListDao(): AreaListDao
    abstract fun shopTypeDao(): ShopTypeDao
    abstract fun pjpListDao(): PjpListDao
    abstract fun modelListDao(): ModelDao
    abstract fun primaryAppListDao(): PrimaryAppDao
    abstract fun secondaryAppListDao(): SecondaryAppDao
    abstract fun leadTypeDao(): LeadTypeDao
    abstract fun stageDao(): StageDao
    abstract fun funnelStageDao(): FunnelStageDao
    abstract fun bsListDao(): BSListDao
    abstract fun quotDao(): QuotationDao
    abstract fun typeListDao(): TypeListDao
    abstract fun memberDao(): MemberDao
    abstract fun memberShopDao(): MemberShopDao
    abstract fun memberAreaDao(): TeamAreaDao
    abstract fun timesheetDao(): TimesheetListDao
    abstract fun clientDao(): ClientListDao
    abstract fun projectDao(): ProjectListDao
    abstract fun activityDao(): ActivityListDao
    abstract fun productDao(): TimesheetProductListDao
    abstract fun shopVisitAudioDao(): ShopVisitAudioDao
    abstract fun taskDao(): TaskDao
    abstract fun batteryNetDao(): BatteryNetStatusDao

    abstract fun activityDropdownDao(): ActivityDropDownDao
    abstract fun typeDao(): TypeDao
    abstract fun priorityDao(): PriorityDao
    abstract fun activDao(): ActivityDao

    abstract fun addDocProductDao(): AddDoctorProductListDao
    abstract fun addDocDao(): AddDoctorDao
    abstract fun addChemistProductDao(): AddChemistProductListDao
    abstract fun addChemistDao(): AddChemistDao

    abstract fun documentTypeDao(): DocumentypeDao
    abstract fun documentListDao(): DocumentListDao

    abstract fun paymenttDao(): PaymentModeDao

    abstract fun entityDao(): EntityTypeDao
    abstract fun partyStatusDao(): PartyStatusDao
    abstract fun retailerDao(): RetailerDao
    abstract fun dealerDao(): DealerDao
    abstract fun beatDao(): BeatDao
    abstract fun assignToShopDao(): AssignToShopDao

    abstract fun visitRemarksDao(): VisitRemarksDao


    //03-09-2021
    abstract fun newOrderGenderDao(): NewOrderGenderDao
    abstract fun newOrderProductDao(): NewOrderProductDao
    abstract fun newOrderColorDao(): NewOrderColorDao
    abstract fun newOrderSizeDao(): NewOrderSizeDao
    abstract fun newOrderScrOrderDao(): NewOrderScrOrderDao

    abstract fun prosDao(): ProspectDao
    abstract fun questionMasterDao(): QuestionDao
    abstract fun questionSubmitDao(): QuestionSubmitDao
    abstract fun addShopSecondaryImgDao(): AddShopSecondaryImgDao

    abstract fun returnDetailsDao(): ReturnDetailsDao
    abstract fun returnProductListDao(): ReturnProductListDao

    abstract fun userWiseLeaveListDao(): UserWiseLeaveListDao

    abstract fun shopFeedbackDao(): ShopFeedbackDao
    abstract fun shopFeedbackTempDao(): ShopFeedbackTepDao
    abstract fun leadActivityDao(): LeadActivityDao

    abstract fun shopDtlsTeamDao(): ShopDtlsTeamDao
    abstract fun billDtlsTeamDao(): BillDtlsTeamDao
    abstract fun orderDtlsTeamDao(): OrderDtlsTeamDao
    abstract fun collDtlsTeamDao(): CollDtlsTeamDao
    abstract fun teamAllShopDBModelDao(): TeamAllShopDBModelDao

    abstract fun distWiseOrderTblDao(): DistWiseOrderTblDao

    abstract fun newGpsStatusDao(): NewGpsStatusDao
    abstract fun shopExtraContactDao(): ShopExtraContactDao

    abstract fun productOnlineRateTempDao(): ProductOnlineRateTempDao


    abstract fun taskManagementDao(): TaskManagementDao
    abstract fun visitRevisitWhatsappStatusDao(): VisitRevisitWhatsappStatusDao
    abstract fun callhisDao(): CallHisDao
    abstract fun companyMasterDao(): CompanyMasterDao
    abstract fun typeMasterDao(): TypeMasterDao
    abstract fun statusMasterDao(): StatusMasterDao
    abstract fun sourceMasterDao(): SourceMasterDao
    abstract fun stageMasterDao(): StageMasterDao
    abstract fun teamListDao(): TeamListDao
    abstract fun contactActivityDao(): ContactActivityDao
    abstract fun scheduleTemplateDao(): ScheduleTemplateDao
    abstract fun modeTemplateDao(): ModeTemplateDao
    abstract fun ruleTemplateDao(): RuleTemplateDao
    abstract fun schedulerMasterDao(): SchedulerMasterDao

    abstract fun schedulerDateTimeDao(): SchedulerDateTimeDao
    abstract fun schedulerContactDao(): SchedulerContactDao
    abstract fun teamAllListDao(): TeamAllListDao
    abstract fun phoneContactDao(): PhoneContactDao
    abstract fun phoneContact1Dao(): PhoneContact1Dao

    abstract fun newProductListDao(): NewProductListDao
    abstract fun newRateListDao(): NewRateListDao
    abstract fun newOrderDataDao(): NewOrderDataDao
    abstract fun newOrderProductDataDao(): NewOrderProductDataDao
    abstract fun opportunityStatusDao(): OpportunityStatusDao
    abstract fun opportunityAddDao(): OpportunityAddDao
    abstract fun opportunityProductDao(): OpportunityProductDao
    abstract fun lmsUserInfoDao(): LmsUserInfoDao
    abstract fun shopAudioDao(): ShopAudioDao
    abstract fun lmsNotiDao(): LMSNotiDao
    abstract fun stockAllDao(): StockAllDao
    abstract fun stockTransDao(): StockTransDao


    companion object {
        var INSTANCE: AppDatabase? = null

        fun initAppDatabase(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DBNAME)
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .addMigrations()
//                        .fallbackToDestructiveMigration()
                        .build()
            }
        }

        fun getDBInstance(): AppDatabase? {

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }



    }


//}


}
package io.wexchain.android.dcc.modules.addressbook.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.Gravity
import io.wexchain.android.common.navigateTo
import io.wexchain.android.common.postOnMainThread
import io.wexchain.android.dcc.App
import io.wexchain.android.dcc.base.BindActivity
import io.wexchain.android.dcc.constant.Extras
import io.wexchain.android.dcc.repo.db.AddressBook
import io.wexchain.android.dcc.repo.db.TransRecord
import io.wexchain.android.dcc.tools.CommonUtils
import io.wexchain.android.dcc.view.dialog.DeleteAddressBookDialog
import io.wexchain.dcc.R
import io.wexchain.dcc.databinding.ActivityAddressDetailBinding
import java.io.Serializable

class AddressDetailActivity : BindActivity<ActivityAddressDetailBinding>() {

    override val contentLayoutId: Int = R.layout.activity_address_detail

    private val ba
        get() = intent.getSerializableExtra(Extras.EXTRA_BENEFICIARY_ADDRESS) as? AddressBook

    private val addressBook: AddressBook?
        get() {
            return ba
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        title = "地址详情"

        if (addressBook == null) {
            postOnMainThread { finish() }
        } else {
            binding.tvWalletAddress.text = addressBook!!.address
            binding.tvAddressName.text = addressBook!!.shortName
            binding.ivAvatar.setImageURI(CommonUtils.str2Uri(addressBook!!.avatarUrl))

            initClick()
        }
    }

    private fun initClick() {
        binding.btEdit.setOnClickListener {
            navigateTo(EditAddressBookActivity::class.java) {
                putExtra(Extras.EXTRA_BENEFICIARY_ADDRESS, addressBook as Serializable)
            }
            finish()
        }

        binding.btDelete.setOnClickListener {

            val deleteDialog = DeleteAddressBookDialog(this)
            deleteDialog.mTvText.text = "确定要删除该地址吗？"
            deleteDialog.mTvText.gravity = Gravity.CENTER_HORIZONTAL
            deleteDialog.setOnClickListener(object : DeleteAddressBookDialog.OnClickListener {
                override fun cancel() {}

                override fun sure() {

                    App.get().passportRepository.getTransRecordByAddress(addressBook!!.address).observe(this@AddressDetailActivity, Observer {
                        var mTrans: ArrayList<TransRecord> = ArrayList()
                        if (null != it && it.isNotEmpty()) {
                            for (item in it) {
                                mTrans.add(TransRecord(item.id, item.address, shortName = "", avatarUrl = "", is_add = 0, create_time = item.create_time, update_time = System.currentTimeMillis()))
                            }
                            App.get().passportRepository.updateTransRecord(mTrans)
                        }
                    })
                    App.get().passportRepository.removeAddressBook(addressBook!!)

                    finish()
                }
            })
            deleteDialog.show()
        }
    }
}

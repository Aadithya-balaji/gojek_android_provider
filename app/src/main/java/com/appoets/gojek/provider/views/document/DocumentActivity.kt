package com.appoets.gojek.provider.views.document

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityDocumentUploadBinding
import com.appoets.gojek.provider.views.uploaddocumentlist.VechileDetailActivity

class  DocumentActivity:BaseActivity<com.appoets.gojek.provider.databinding.ActivityDocumentUploadBinding>(),DocumentNavigator,View.OnClickListener{


    private  var activityDocumentUploadBinding:ActivityDocumentUploadBinding?=null
    private  lateinit var  tvLeftHeader:TextView
    private  lateinit var ivBackArrow:ImageView
    private  lateinit var  tvRightHeader:TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_document_upload
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityDocumentUploadBinding=mViewDataBinding as ActivityDocumentUploadBinding
        val documentViewModel=DocumentViewModel()
        documentViewModel.setNavigator(this)
        activityDocumentUploadBinding!!.documentviewmodel=documentViewModel

        tvLeftHeader=findViewById(R.id.tv_header)
        ivBackArrow=findViewById(R.id.iv_back)
        tvRightHeader=findViewById(R.id.tv_right_header)


        tvLeftHeader.visibility= View.GONE
        ivBackArrow.visibility=View.VISIBLE
        tvRightHeader.visibility=View.VISIBLE

        tvRightHeader.setText(resources.getString(R.string.header_label_document_upload))
        ivBackArrow.setOnClickListener(this)

    }

    override fun uploadImages() {

        val intent= Intent(this@DocumentActivity,VechileDetailActivity::class.java)
        startActivity(intent)

    }

    override fun uploadPDF() {
        val intent= Intent(this@DocumentActivity,VechileDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        finish()
    }


}
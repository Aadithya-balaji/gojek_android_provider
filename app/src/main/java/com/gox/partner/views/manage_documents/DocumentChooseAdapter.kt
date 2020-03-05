package com.gox.partner.views.manage_documents


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import com.gox.base.utils.Utils.dateHasExpired
import com.gox.base.utils.Utils.userDateFormat
import com.gox.partner.R
import com.gox.partner.models.ListDocumentResponse
import com.gox.partner.models.ManageServicesResponseModel.ResponseData
import kotlinx.android.synthetic.main.item_document.view.*
import kotlinx.android.synthetic.main.item_document_type.view.*


class DocumentChooseAdapter() : BaseExpandableListAdapter() {
    private var documentList: List<ResponseData> = ArrayList()
    override fun getGroup(groupPosition: Int): Any {
        return documentList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater = parent?.context!!
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_document_type, null)

        }
        convertView!!.tvDocumentType.text = (getGroup(groupPosition) as ResponseData).adminServiceName
        convertView.ivGroupIndicator.isSelected = isExpanded
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        if (documentList[groupPosition].documents.isNullOrEmpty())
            return 0
        return documentList[groupPosition].documents.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return documentList[groupPosition].documents[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater = parent?.context!!
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_document, null)

        }
        val item = (getChild(groupPosition, childPosition) as ListDocumentResponse.ResponseData)

        convertView!!.tvDocumentName.text = item.name
        if (item.provider_document != null) {
            if (item.is_expire == 1) {
                convertView.tvExpire.text = parent!!.context.getString(R.string.expire_at).plus(
                        userDateFormat(item.provider_document!!.expires_at)
                )
                if (dateHasExpired(item.provider_document!!.expires_at)) {
                    convertView.ivStatus.setImageResource(R.drawable.ic_info)
                    convertView.tvExpire.setTextColor(ContextCompat.getColor(parent.context, R.color.colorLightRed))
                    convertView.ivStatus.setColorFilter(ContextCompat.getColor(parent.context, R.color.colorLightRed))
                }else{
                    convertView.ivStatus.setImageResource(R.drawable.ic_checked)
                    convertView.tvExpire.setTextColor(ContextCompat.getColor(parent.context, R.color.colorGray))
                    convertView.ivStatus.setColorFilter(ContextCompat.getColor(parent.context, R.color.colorGreen))
                }
            }else{
                convertView.ivStatus.setImageResource(R.drawable.ic_checked)
                convertView.ivStatus.setColorFilter(ContextCompat.getColor(parent!!.context, R.color.colorGreen))
            }
        } else {
            convertView.ivStatus.setImageResource(R.drawable.ic_info)
            convertView.ivStatus.setColorFilter(ContextCompat.getColor(parent!!.context, R.color.colorLightRed))
            convertView.tvExpire.text == parent.context.getString(R.string.expire_at).plus("--")
        }
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return documentList.size
    }

    fun setData(documentList: List<ResponseData>) {
        this.documentList = documentList
        notifyDataSetChanged()
    }

    fun setItem(groupIndex: Int, childIndex: Int,doc: ListDocumentResponse.ResponseData.ProviderDocument) {
        documentList[groupIndex].documents[childIndex].provider_document = doc
        notifyDataSetInvalidated()
    }
}
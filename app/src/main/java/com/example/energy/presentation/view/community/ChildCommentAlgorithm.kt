package com.example.energy.presentation.view.community

import com.example.energy.data.repository.community.Comment

// 댓글 정렬 알고리즘
fun makeChildComment(dataSet : ArrayList<Comment>): ArrayList<Comment>{
    val newDataSet = ArrayList<Comment>() //새로운 댓글 리스트
    val hashList = HashMap<Int,ArrayList<Comment>>() //부모 뷰에 해당하는 자식 뷰들을 관리하기 위한 리스트

    //같은 부모뷰를 가지는 댓글들을 해쉬함수를 통해서 묶어줌
    for (data in dataSet){
        val parentId = data.parentCommentId ?: -1

        if (parentId<1){ //부모 뷰
            newDataSet.add(data)
        }else{ //자식 뷰
            if (hashList.containsKey(parentId)){ //이미 존재
                hashList[parentId]!!.add(data)
            }else{ //0번째 인덱스 추가
                hashList[parentId] = arrayListOf(data)
            }
        }
    }

    //자식 뷰들을 부모 뷰가 추가된 리스트에 순서대로 넣어줌
    var i = 0
    while(i < newDataSet.size){
        // 자식 뷰 발견 -> 위치에 자식뷰 리스트 추가
        if (hashList.containsKey(newDataSet[i].commentId)){
            val subData = hashList[newDataSet[i].commentId]!!
            newDataSet.addAll(i+1,subData)
            i+=subData.size-1
        }
        i+=1
    }

    return newDataSet
}


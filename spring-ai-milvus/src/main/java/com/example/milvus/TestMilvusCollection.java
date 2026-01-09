package com.example.milvus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddCollectionFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.utility.request.FlushReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.GetReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.GetResp;
import io.milvus.v2.service.vector.response.QueryResp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestMilvusCollection {
    static final String MILVUS_URI = "http://localhost:19530";
    static final String TIKEN = "root:Milvus";

    public static void main(String[] args) {
        //连接到milvus
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(MILVUS_URI)
                .token(TIKEN)
                .build();
        MilvusClientV2 client = new MilvusClientV2(connectConfig);
        //创建collection
        String collectionName = "testCollection";
        //创建collection
        createCollection(client, collectionName);
        //列出collection
        List<String> collectionNames = client.listCollections().getCollectionNames();
        System.out.println("collectionNames = " + collectionNames);
        //向collection插入数据
        insertDataIntoCollection(client, collectionName);
        //查询数据
        GetResp getResp = client.get(
                GetReq.builder()
                        .collectionName(collectionName)
                        .ids(Arrays.asList(1, 2, 3))
                        .outputFields(Arrays.asList("id", "vector", "color"))
                        .build()
        );
        for (QueryResp.QueryResult getResult : getResp.getResults) {
            System.out.println("数据：" + getResult);
        }
        //删除数据
        DeleteResp delete = client.delete(
                DeleteReq.builder()
                        .collectionName(collectionName)
                        .ids(Arrays.asList(1, 2, 3))
                        .build()
        );
        System.out.println("delete = " + delete);
    }

    //插入数据
    private static void insertDataIntoCollection(MilvusClientV2 client, String collectionName) {
        Gson gson = new Gson();
        List<JsonObject> data = Arrays.asList(
                gson.fromJson("{\"id\": 0, \"vector\": [0.3580376395471989, -0.6023495712049978, 0.18414012509913835, -0.26286205330961354, 0.9029438446296592], \"color\": \"pink_8683\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 1, \"vector\": [0.19886812562848388, 0.06023560599112088, 0.6976963061752597, 0.2614474506242501, 0.838729485096104], \"color\": \"red_7025\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 2, \"vector\": [0.43742130801983836, -0.5597502546264526, 0.6457887650909682, 0.7894058910881185, 0.20785793220625592], \"color\": \"orange_6781\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 3, \"vector\": [0.3172005263489739, 0.9719044792798428, -0.36981146090600725, -0.4860894583077995, 0.95791889146345], \"color\": \"pink_9298\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 4, \"vector\": [0.4452349528804562, -0.8757026943054742, 0.8220779437047674, 0.46406290649483184, 0.30337481143159106], \"color\": \"red_4794\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 5, \"vector\": [0.985825131989184, -0.8144651566660419, 0.6299267002202009, 0.1206906911183383, -0.1446277761879955], \"color\": \"yellow_4222\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 6, \"vector\": [0.8371977790571115, -0.015764369584852833, -0.31062937026679327, -0.562666951622192, -0.8984947637863987], \"color\": \"red_9392\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 7, \"vector\": [-0.33445148015177995, -0.2567135004164067, 0.8987539745369246, 0.9402995886420709, 0.5378064918413052], \"color\": \"grey_8510\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 8, \"vector\": [0.39524717779832685, 0.4000257286739164, -0.5890507376891594, -0.8650502298996872, -0.6140360785406336], \"color\": \"white_9381\"}", JsonObject.class)
                , gson.fromJson("{\"id\": 9, \"vector\": [0.5718280481994695, 0.24070317428066512, -0.3737913482606834, -0.06726932177492717, -0.6980531615588608], \"color\": \"purple_4976\"}", JsonObject.class));
        //插入数据
        client.insert(
                InsertReq.builder()
                        .collectionName(collectionName)
                        .data(data)
                        .build()
        );
        //刷新数据，否则查询不到
        client.flush(
                FlushReq.builder()
                        .collectionNames(Collections.singletonList(collectionName))
                        .build()
        );
        System.out.println("插入数据成功");
    }

    private static void createCollection(MilvusClientV2 client, String collectionName) {
        //创建schema
        CreateCollectionReq.CollectionSchema schema = MilvusClientV2.CreateSchema()
                .addField(
                        AddCollectionFieldReq.builder()
                                .fieldName("id")
                                .dataType(DataType.Int64)
                                .isPrimaryKey(Boolean.TRUE)
                                .autoID(Boolean.FALSE)
                                .build()
                )
                .addField(
                        AddCollectionFieldReq.builder()
                                .fieldName("vector")
                                .dataType(DataType.FloatVector) //向量列
                                .dimension(5)
                                .build()

                )
                .addField(
                        AddCollectionFieldReq.builder()
                                .fieldName("color")
                                .dataType(DataType.VarChar)
                                .maxLength(512)
                                .build()

                );

        //构建索引
        List<IndexParam> indexParams = new ArrayList<>();
        IndexParam vectorIndex = IndexParam.builder()
                .fieldName("vector")
                //指定索引类型
                .indexType(IndexParam.IndexType.IVF_FLAT)
                //余玄相似度
                .metricType(IndexParam.MetricType.COSINE)
                .build();
        indexParams.add(vectorIndex);

        if (client.hasCollection(HasCollectionReq.builder().collectionName(collectionName).build())) {
            System.out.println("Collection already exists");
            return;
        }

        //创建collection
        client.createCollection(CreateCollectionReq.builder()
                .collectionName(collectionName)
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build()
        );
    }
}

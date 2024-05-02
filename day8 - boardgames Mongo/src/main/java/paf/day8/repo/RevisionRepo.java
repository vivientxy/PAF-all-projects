package paf.day8.repo;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RevisionRepo {
    
    @Autowired
    private MongoTemplate template;

    // db.tvshows.findOne()
    public Optional<Document> findOne() {
        Query query = Query.query(Criteria.where("_id").exists(true));
        return Optional.ofNullable(template.findOne(query, Document.class));
    }

    // db.tvshows.find()
    public Optional<List<Document>> find() {
        Query query = Query.query(Criteria.where("_id").exists(true));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // db.tvshows.find({name: 'Fargo'})
    public Optional<List<Document>> findByName(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // multiple search criteria:
    // db.tvshows.find({
    //     name: {$regex: 'battle', $options: 'i'}, // insensitive search
    //     status: 'Ended',
    //     "rating.average": { $gte: 5.0, $lte: 7.0}
    // })
    public Optional<List<Document>> findByMultipleCriteria(String name, String status, float greaterThan, float lessThan) {
        Criteria criteria = Criteria.where("name").regex(name, "i")
                .and("status").is(status)
                .and("rating.average").gte(greaterThan).lte(lessThan);
        Query query = Query.query(criteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // search by _id:
    // db.tvshows.find({
    //     _id: ObjectId("6621d4137f50ea5760fbbc37")
    // })
    public Optional<Document> findByObjectId(ObjectId _id) {
        Query query = Query.query(Criteria.where("_id").is(_id));
        return Optional.ofNullable(template.findOne(query, Document.class));
    }

    // // logical operators:
    // db.tvshows.find({
    //     $or: [
    //         { year: { $gte: 2020 } },
    //         { year: { $lte: 1800 } }
    //     ]
    // })
    public Optional<List<Document>> findOrOperator(float greaterThan, float lessThan) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("year").gte(greaterThan),
                Criteria.where("year").lte(lessThan)
                );
        Query query = Query.query(criteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // db.tvshows.find({
    //     genres: {
    //         $all: ['Drama','Thriller'] // must have ALL the listed attributes
    //     },
    //     // embedded attributes
    //     "schedule.days" : {
    //         $in: [ 'Friday','Saturday' ] // only need to have ONE OF the listed attributes
    //     },
    //     "rating.average": { $gte: 7.0},
    //     awards: { $exists: false }
    // })
    public Optional<List<Document>> findAllAndIn() {
        Criteria allCriteria = new Criteria("genres").all("Drama","Thriller");
        Criteria inCriteria = new Criteria("schedule.days").in("Friday","Saturday");
        Criteria gteCriteria = new Criteria("rating.average").gte(7.0);
        Criteria existsCriteria = new Criteria("awards").exists(false);

        Query query = Query.query(allCriteria)
                .addCriteria(inCriteria)
                .addCriteria(gteCriteria)
                .addCriteria(existsCriteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // distinct
    // db.tvshows.distinct("type", {"rating.average": { $gte:7 }})
    public Optional<List<Document>> findDistinct() {
        Criteria criteria = Criteria.where("rating.average").gte(7);
        Query query = Query.query(criteria);
        return Optional.ofNullable(
            template.findDistinct(query, "type", "tvshows", Document.class));
    }

    // // count
    // db.tvshows.find({ "rating.average": { $gte: 5 } }).count()
    public Long getCount() {
        Criteria criteria = new Criteria("rating.average").gte(5);
        Query query = Query.query(criteria);
        return template.count(query, Document.class);
    }

    // // projection, sort, limit
    // db.tvshows.find({
    //     "rating.average": { $gte: 7.0 }
    // })
    // .projection({name:1, genres:1, _id:0}) // explicitly stating you dw _id
    // .sort({"rating.average":-1}) // descending order
    // .limit(10)
    public Optional<List<Document>> findProjectionSortLimit() {
        Criteria criteria = new Criteria("rating.average").gte(7);
        Query query = Query.query(criteria)
                .with(Sort.by(Direction.DESC, "rating.average"))
                .limit(10);
        query.fields().exclude("_id").include("name", "genres");
        return Optional.ofNullable(
            template.find(query, Document.class, "tvshows"));
    }

    // // create
    // db.persons.insert({
    //     name: "Mary",
    //     age: 25,
    //     gender: "F",
    //     hobbies: [ "sleeping","eating","coding" ]
    // });
    

    // // update
    // db.persons.updateOne(
    //     {gender : "F"},
    //     {
    //         $set: { name : "Emily2" },
    //         $inc: { age : 2 } // increment by 2
    //     }
    // );

    // // delete
    // db.persons.deleteOne({
    //     _id: ObjectId("66272616cb8054bb5fe125a9")
    // })

    // // create index
    // db.movies.createIndex({
    //     "$**" : "text"
    // });

    // db.movies.createIndex({
    //     Title : "text"
    // });

    // // drop indexes
    // db.movies.dropIndexes();

    // // search using index
    // db.movies.find({
    //     $text : {
    //         $search : "aVatAr",
    //         $caseSensitive : false
    //     }
    // });

    // // search for embedded document
    // db.inventory.find(
    //     { "instock.warehouse" : "Bedok" }
    // )

    // // day 27 - slide 28 (condition applies to any document in the array)
    // db.inventory.find({
    //     $and : [
    //         { "instock.warehouse" : "Bedok" },
    //         { "instock.qty" : { $gte:15 } }
    //     ]
    // })

    // // day 27 - slide 28 (element match)
    // db.inventory.find({
    //     instock : {
    //         $elemMatch: {warehouse: "Bedok", qty: {$gte:15}}
    //     }
    // })

    // // day 27 - slide 29. only first record is updated
    // db.inventory.update(
    //     {"instock.warehouse": "Ang Mo Kio"},
    //     {$inc: { "instock.$[].qty": 10 }}
    // );

    // // day 27 - slide 30. push (add on) new embedded document
    // db.inventory.update(
    //     {item:"postcard"},
    //     {$push: {instock: {warehouse: "Ubi", qty:100 }}}
    // )

    // // day 27 - slide 30. pop (take out) embedded document
    // db.inventory.update(
    //     {item: "postcard"},
    //     {$pop: {instock: -1}} // remove first element in the array. 1 is remove from the back
    // )

    // // day 27 - slide 31. clean up data
    // db.inventory.updateMany(
    //     {"instock.warehouse" : "Ang Mo Kio"},
    //     {$set: { "instock.$[elem].qty": 0 }},
    //     {arrayFilters: [
    //         {"elem.qty": {$lt:0}}
    //     ]}
    // )

    // // ============ AGGREGATION ============

    // // filter + project:
    // db.movies.aggregate([
    //     {$match: {Rated:"PG"}},
    //     {$project: {_id:0, Title:1, Year:1, Rated:1, summary:"$Awards"}}
    // ])

    // // group + sort:
    // db.movies.aggregate([
    //     {$group: {_id:"$Rated",count:{$sum:1},titles:{$push:"$Title"}}},
    //     {$sort: {count:-1}}
    // ])

    // // projection + concatenation:
    // db.movies.aggregate([
    //     {$project: {
    //         _id:0, 
    //         title:{
    //             $concat: ["$Title"," (","$Rated",")"]
    //         },
    //         summary:"$Awards"
    //         }},
    //     {$sort:{title:1}}
    // ])

    // // unwind arrays
    // db.movies.aggregate([
    //     {$unwind:"$Actors"}
    // ])

    // // unwind arrays + group
    // db.movies.aggregate([
    //     {$unwind:"$Actors"},
    //     {$group: {
    //         _id:"$Actors",
    //         titles:{$push:"$Title"},
    //         titles_count:{$sum:1}
    //     }},
    //     {$sort:{"_id":1}}
    // ])

    // // bucket classification
    // db.games.aggregate([
    //     {$bucket: {
    //         groupBy: "$year",
    //         boundaries: [1990,2000,2010,2020],
    //         default: "Others",
    //         output: {
    //             count: {$sum:1},
    //             titles: {$push:"$name"}
    //         }
    //     }},
    //     {$unwind:"$titles"}
    // ])

    // // bucket classification + unwind
    // db.movies.aggregate([
    //     {$unwind:"$Genre"},
    //     {$bucket: {
    //         groupBy: "$Genre",
    //         boundaries: ["Adventure","Biography","Comedy","Drama","Fantasy","Horror","Sci-Fi"],
    //         default: "ZZZ",
    //         output: {
    //             count: {$sum:1},
    //             titles: {$push:{$concat:["$Title"," (","$Rated",")"]}}
    //         }
    //     }},
    //     {$unwind:"$titles"},
    //     {$sort:{titles:-1}}
    // //    ,{$out:{db:"test",coll:"moviesTest"}}
    // ])

    // // embedding a reference document
    // db.games.aggregate([
    //     {$match:{name:"Catan"}},
    //     {$lookup:{
    //         from:"comments",
    //         foreignField:"gid",
    //         localField:"gid",
    //         as:"reviews"
    //     }},
    //     {$unwind:"$reviews"}
    // ])

    // // $lookup with pipeline
    // db.movies.aggregate([
    //     {$match: {Rated:"PG"}},
    //     {$lookup: {
    //         from: "reviews",
    //         foreignField: "movie_id",
    //         localField:"_id",
    //         pipeline: [
    //             {$sort: {user:1}},
    //             {$limit: 2},
    //             {$project: {_id:0,user:1,review:1}}
    //         ],
    //         as: "Reviews"
    //     }},
    //     {$project: {_id:1, Title:1,Year:1,Rated:1,Genre:1,Reviews:1}},
    //     {$sort: {Title:1}}
    // ]);

    // // converting data type (from String to Int/Bool)
    // db.movies.aggregate([
    //     {$set:{
    //         Year:{$toInt:"$Year"},
    //         Response:{$toBool:"$Response"}
    //     }}
    // ])

    // // converting data type (using $convert)
    // db.movies.aggregate([
    //     {$convert:{
    //         input:"",
    //         to:"",
    //         onError:"",
    //         onNull:""
    //     }}
    // ])

    // // ============ REVISION ===============

    // // filter and create new collection
    // db.listings_and_reviews.aggregate([
    //     {$match: {"address.country": {$regex: "australia", $options: "i"}}},
    //     {$out: {db: "bedandbreakfast", coll: "listings_test"}}
    // ]);

    // // drop collection
    // db.listings_and_reviews.drop();

    // // join collections, replace strings, and project as flatmap
    // db.listings.aggregate([
    //     {$unwind:"$reviews"},
    //     {$project:{
    //         _id:"$reviews._id",
    //         date:"$reviews.date",
    //         listing_id:"$reviews.listing_id",
    //         reviewer_name: {$replaceAll:{
    //             input: "$reviews.reviewer_name", 
    //             find: ",",
    //             replacement: ""
    //         }},
    //         comments: {$replaceAll:{
    //             input: {$replaceAll:{
    //                 input: "$reviews.comments",
    //                 find: "\n",
    //                 replacement: ""
    //             }},
    //             find: "\r",
    //             replacement: ""
    //         }}        
    //     }},
    //     {$out: {db: "bedandbreakfast", coll: "reviews"}}
    // ])

    // // remove a column from collection
    // db.listings.updateMany(
    // {},
    // {$unset: {reviews: 1}}
    // )

}

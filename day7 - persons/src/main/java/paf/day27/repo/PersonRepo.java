package paf.day27.repo;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import paf.day27.model.Person;

@Repository
public class PersonRepo {

    @Autowired
    MongoTemplate template;

    // Create
    // - insert = if got _id that already exists in DB --> throw error
    // - save = if got _id that already exists in DB --> updates record
    public Person insertPerson(Person person) {
        return template.insert(person);
    }
    public Person savePerson(Person person) {
        return template.save(person);
    }

    // Retrieve
    public List<Person> findAll() {
        return template.findAll(Person.class);
    }

    // Update
    public Person updatePerson(Person person) {
        return template.save(person);
    }

    // Delete
    public void deletePerson(Person person) {
        template.remove(person);
    }

    // clean _id field (replace int with ObjectId)
    public void findAndUpdatePerson(ObjectId objId, Person person) {
        Query query = Query.query(Criteria.where("_id").is(objId));
        Update updateOperation = new Update()
            .set("name", person.getName())
            .inc("age", 1);
        UpdateResult result = template.updateMulti(query, updateOperation, "persons");
        System.out.println("Document updated: " + result.getModifiedCount());
    }

    // 
    public void update(String oldName, String newName, List<String> hobbies) {
        Query query = new Query(Criteria.where("name").is(oldName));
		// Update updateOps = new Update()
		// 		.set("name", "Emily Ng")
		// 		.push("hobbies").each(List.of("movies","prawning").toArray());
        Update updateOps = new Update()
                .set("name", newName)
                .push("hobbies", hobbies);
		UpdateResult updateResult = template.upsert(query, updateOps, "persons");
        System.out.println(updateResult.getModifiedCount());
    }

    public UpdateResult updatePerson(String conditionKey, String conditionValue, Person personRecord, String collectionName) {
        Query query = new Query(Criteria.where(conditionKey).is(conditionValue));
        Update updateOps = new Update()
            .set("name", personRecord.getName())
            .set("age", personRecord.getAge())
            .push("hobbies").each(personRecord.getHobbies());
        UpdateResult updateResult = template.upsert(query, updateOps, collectionName);
        return updateResult;
    }

    // retrieve with TextCriteria
    // must have Index in the database
    /*
     * db.movies.createIndex({
     *      Title : "text"
     *  });
     */
    public List<Person> retrievePersons(String searchString) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchString);
        TextQuery textQuery = TextQuery.queryText(textCriteria);
        List<Person> results = template.find(textQuery, Person.class, "persons");
        return results;
    }

    public List<Document> retrieveDocuments(String searchString) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchString);
        TextQuery textQuery = TextQuery.queryText(textCriteria);
        List<Document> results = template.find(textQuery, Document.class, "persons");
        return results;
    }

}

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { client } from "@/lib/api-client";
import Link from "next/link";

export default async function Challenges() {
  const { data, error } = await client.GET("/challenges");

  if (error) {
    throw new Error("Could not find your challenges");
  }

  return (
    <>
      <h1>Your challenges</h1>
      <div className="grid grid-cols-1 gap-y-6 my-6">
        {data.map((challenge) => (
          <Link
            key={challenge.id}
            href={`/challenges/${challenge.id}`}
            className="max-w-2xs"
          >
            <Card>
              <CardHeader>
                <CardTitle>{challenge.name}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-2">
                  <p>Status</p>
                  <p>{challenge.status}</p>
                  <p>Duration (days)</p>
                  <p>{challenge.durationDays || "Not set"}</p>
                  <p>Start date</p>
                  <p>{challenge.starDate || "Not set"}</p>
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>
    </>
  );
}

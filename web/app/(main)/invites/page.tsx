import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { client } from "@/lib/api-client";
import AcceptButton from "./components/AcceptButton";
import DeclineButton from "./components/DeclineButton";

export default async function Page() {
  const { error, data } = await client.GET("/invites");

  if (error) {
    throw new Error("Could not get your invites");
  }

  return (
    <div>
      <h1>Your invites</h1>
      <div>
        {data.map(({ id, challenge, inviteSender }) => (
          <Card key={id}>
            <CardHeader>
              <CardTitle>{challenge.name}</CardTitle>
            </CardHeader>
            <CardContent>
              <p>
                Invited by {inviteSender.username} (
                {`${inviteSender.firstName} ${inviteSender.lastName}`})
              </p>
            </CardContent>
            <CardFooter className="grid grid-cols-2 w-fit gap-x-2">
              <AcceptButton inviteId={id} challengeId={challenge.id} />
              <DeclineButton inviteId={id} />
            </CardFooter>
          </Card>
        ))}
      </div>
    </div>
  );
}

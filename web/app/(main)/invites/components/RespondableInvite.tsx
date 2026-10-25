import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import User from "@/components/User";
import { components } from "@/types/api";
import AcceptButton from "./AcceptButton";
import DeclineButton from "./DeclineButton";

type RespondableInviteProps = {
  invite: components["schemas"]["ReceivedInviteResponse"];
};

export default function RespondableInvite({ invite }: RespondableInviteProps) {
  const { id, challenge, inviteSender } = invite;

  return (
    <Card key={id}>
      <CardHeader>
        <CardTitle>
          {challenge.name} - {challenge.durationDays} days
        </CardTitle>
      </CardHeader>
      <CardContent className="flex items-center gap-x-2">
        <p>Invited by:</p>
        <User user={inviteSender} />
      </CardContent>
      <CardFooter className="grid grid-cols-2 w-fit gap-x-2">
        <AcceptButton inviteId={id} challengeId={challenge.id} />
        <DeclineButton inviteId={id} />
      </CardFooter>
    </Card>
  );
}

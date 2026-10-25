import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import User from "@/components/User";
import { components } from "@/types/api";
import { DeleteButton } from "./DeleteButton";

type NonRespondableInviteProps = {
  invite: components["schemas"]["ReceivedInviteResponse"];
};

export function NonRespondableInvite({ invite }: NonRespondableInviteProps) {
  const { id, challenge, inviteSender } = invite;
  return (
    <Card key={id}>
      <CardHeader>
        <CardTitle>
          {challenge.name} - {challenge.durationDays} days
        </CardTitle>
        <CardAction>
          <DeleteButton inviteId={id} />
        </CardAction>
      </CardHeader>
      <CardContent className="flex flex-col gap-y-2">
        <div className="flex items-center gap-x-2">
          <p>Invited by:</p>
          <User user={inviteSender} />
        </div>
        <p>Challenge started: {challenge.starDate ?? "Unknown"}</p>
      </CardContent>
    </Card>
  );
}
